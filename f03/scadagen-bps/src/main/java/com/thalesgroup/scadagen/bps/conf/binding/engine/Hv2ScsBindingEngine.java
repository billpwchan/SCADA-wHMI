package com.thalesgroup.scadagen.bps.conf.binding.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.binding.BooleanBinding;
import com.thalesgroup.scadagen.binding.BooleanToIntegerBinding;
import com.thalesgroup.scadagen.binding.IdentityBinding;
import com.thalesgroup.scadagen.binding.Input;
import com.thalesgroup.scadagen.binding.IntegerEnumerationRangeBinding;
import com.thalesgroup.scadagen.binding.MonoInputBinding;
import com.thalesgroup.scadagen.binding.MultiInputBinding;
import com.thalesgroup.scadagen.binding.StringEnumerationRangeBinding;
import com.thalesgroup.scadagen.binding.StringToIntegerBinding;
import com.thalesgroup.scadagen.bps.conf.binding.data.HvDataWrapper;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * binding engine which is able to convert data from hypervisor entity notification to scs db point value
 */
public class Hv2ScsBindingEngine {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(Hv2ScsBindingEngine.class);

    /** loader of the binding configuration */
    private final BindingLoader bindingLoader_;
    
    /** helper to manipulate the model */
    private final IDataHelper dataHelper_;
    
    /** convenient cache to be able to easily implements the multi input computers */
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, IData>> multiInputCache_
        = new ConcurrentHashMap<String, ConcurrentHashMap<String, IData>>();
    
    /**
     * Constructor
     * @param bindingLoader loader of the binding configuration
     * @param dataHelper helper to manipulate the model
     */
    public Hv2ScsBindingEngine(final BindingLoader bindingLoader, final IDataHelper dataHelper) {
        bindingLoader_ = bindingLoader;
        dataHelper_ = dataHelper;
    }
    
    
    public IData getScsValue(final AbstractEntityStatusesType entity, final String eqpId, final String attributeName) {
    	IData toReturn = null;
    	
    	//String entityId = entity.getId();
    	
    	try {
			AbstractAttributeType att = dataHelper_.getAttribute(entity, attributeName);
			IData inputHvData = (IData) new HvDataWrapper(eqpId, attributeName, att);
			HashSet<AttributeBinding> bindingSet = new HashSet<AttributeBinding>(bindingLoader_.getDescription(inputHvData));
			if (bindingSet.isEmpty()) {
				throw new HypervisorException("Unable to get binding for input attribute " + attributeName);
			}
			// Assume only 1 binding that matches. Use the first binding.
			AttributeBinding binding = bindingSet.iterator().next();
			toReturn = bindingLoader_.getConverterManager().convert(binding, inputHvData);
			
		} catch (EntityManipulationException e) {
			LOGGER.error("Error get value from binding for attribute {}. {}", attributeName, e);
		} catch (HypervisorException e) {
			LOGGER.error("Error get value from binding for attribute {}. {}", attributeName, e);
		}
    	
    	return toReturn;
    }

	public IData getScsValue(AbstractEntityStatusesType entity, String eqpId, AttributeBinding binding) {
		IData data = null;
		
		if (binding instanceof MonoInputBinding) {
			String attName = ((MonoInputBinding) binding).getInput().getName();
			LOGGER.trace("Get value from input [{}] MonoInputBinding", attName);
			data = getScsValue(entity, eqpId, attName);
		} else if (binding instanceof MultiInputBinding) {
			MultiInputBinding mBinding = (MultiInputBinding)binding;
			Map<String, IData> dataMap = new HashMap<String, IData>();
			
			for (Input input: mBinding.getInputs()) {
				String attName = input.getName();
				LOGGER.debug("binding input = [{}]", attName);
				AbstractAttributeType att = null;
				try {
					att = dataHelper_.getAttribute(entity, attName);
				} catch (EntityManipulationException e) {
					LOGGER.error("Error get value from binding for attribute {}. {}", attName, e);
				}
				IData inputHvData = (IData) new HvDataWrapper(entity.getId(), attName, att);
				dataMap.put(attName, inputHvData);
			}
			data = getMergedData(entity, mBinding, dataMap);
		}
		return data;
	}
    
    public Collection<AttributeBinding> getAttributeBindings(String bindingId, String hvInputAttribute) {
    	return bindingLoader_.getAttributeBindings(new CompositeKey(bindingId, hvInputAttribute));
    }
    
    public Collection<AttributeBinding> getAttributeBindings(String bindingId) {
    	return bindingLoader_.getAttributeBindings(bindingId);
    }

	public String getScsValueType(AttributeBinding binding) {
		if (binding instanceof StringToIntegerBinding) {
			return "INT";
		} else if (binding instanceof BooleanToIntegerBinding) {
			return "INT";
		} else if (binding instanceof BooleanBinding) {
			return "BOOL";
		} else if (binding instanceof IntegerEnumerationRangeBinding) {
			return "INT";
		} else if (binding instanceof StringEnumerationRangeBinding) {
			return "STRING";
		} else if (binding instanceof IdentityBinding) {
			return ((IdentityBinding)binding).getInput().getType().value();
		} else if (binding instanceof MultiInputBinding) {
			return bindingLoader_.getComputerManager().getComputedDataType((MultiInputBinding)binding);
		}
		return null;
	}

    
    /**
     * convert the mono input 
     * @param description binding description
     * @param dataMap dataMap to use (in fact only one entry will be used based on the desciption)
     * @return the converted data
     */
//    private IData getMergedData(final MonoInputBinding description, Map<String, IData> dataMap) {
//        return bindingLoader_.getConverterManager().convert(description, dataMap.get(description.getInput().getName()));
//    }
    
    /**
     * convert the multiple input 
     * @param entity entity to use
     * @param description binding description
     * @param dataMap dataMap to use (first merge, then convert)
     * @return the converted data
     */
    private IData getMergedData(AbstractEntityStatusesType entity, final MultiInputBinding description, 
            final Map<String, IData> dataMap) {
    	LOGGER.debug("getMergedData for entity [{}] target [{}]", entity.getId(), description.getId());
        
        //check indexing
        ConcurrentHashMap<String, IData> dataCache = multiInputCache_.get(entity.getId());
        if (dataCache == null) {
            dataCache = new ConcurrentHashMap<String, IData>();
            multiInputCache_.putIfAbsent(entity.getId(), dataCache);
        }
        //consolidate caching
        dataCache.putAll(dataMap);
        
        //send the consolidated data to the computer
        return bindingLoader_.getComputerManager().compute(dataHelper_, entity, description, dataCache);
    }
   
}
