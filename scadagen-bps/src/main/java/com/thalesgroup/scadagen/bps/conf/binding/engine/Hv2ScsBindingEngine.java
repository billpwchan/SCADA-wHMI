package com.thalesgroup.scadagen.bps.conf.binding.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.configuration.NotificationEntityBuilder;
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractConfiguredEntityStatusesType;
import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.binding.BooleanBinding;
import com.thalesgroup.scadagen.binding.BooleanToIntegerBinding;
import com.thalesgroup.scadagen.binding.IntegerEnumerationRangeBinding;
import com.thalesgroup.scadagen.binding.MultiInputBinding;
import com.thalesgroup.scadagen.binding.StringEnumerationRangeBinding;
import com.thalesgroup.scadagen.binding.StringToIntegerBinding;
import com.thalesgroup.scadagen.bps.conf.binding.builder.AttributeBuilder;
import com.thalesgroup.scadagen.bps.conf.binding.data.HvDataWrapper;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * binding engine which is able to convert data from hypervisor entity notification to scs db point value
 */
public class Hv2ScsBindingEngine {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(Hv2ScsBindingEngine.class);
    
    /** notification builder to create the empty notification entity */
    //private final NotificationEntityBuilder builder_;
    
    /** attribute builder to create the hypervisor attributes */
    //private final AttributeBuilder attBuilder_;
    
    /** loader of the binding configuration */
    private final BindingLoader bindingLoader_;
    
    /** helper to manipulate the model */
    private final IDataHelper dataHelper_;
    
    /** convenient cache to be able to easily implements the multi input computers */
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, IData>> multiInputCache_
        = new ConcurrentHashMap<String, ConcurrentHashMap<String, IData>>();
    
    /**
     * Constructor
     * @param builder notification builder to create the empty notification entity
     * @param bindingLoader loader of the binding configuration
     * @param dataHelper helper to manipulate the model
     * @param attBuilder attribute builder to create the hypervisor attributes
     */
    public Hv2ScsBindingEngine(final NotificationEntityBuilder builder,
                         final BindingLoader bindingLoader, 
                         final IDataHelper dataHelper, 
                         final AttributeBuilder attBuilder) {
        //builder_ = builder;
        bindingLoader_ = bindingLoader;
        dataHelper_ = dataHelper;
        //attBuilder_ = attBuilder;
    }
    
    /**
     * This method is for lazy people. It is not the most efficient implementation.
     * It is advised to use the {@link #createEntities(Map)} method instead
     * @param inputs "raw input"
     * @return the entities ready to be notified
     */
//    public Collection<AbstractConfiguredEntityStatusesType> createEntities(final Collection<IData> inputs) {
//        return createEntities(prepareData(inputs));
//    }
    
    /**
     * prepare the data to match the parsing mechanism
     * @param inputs the raw data
     * @return the prepared data
     */
//    private Map<String, Map<String, IData>> prepareData(final Collection<IData> inputs) {
//        final Map<String, Map<String, IData>> toReturn = new LinkedHashMap<String, Map<String, IData>>();
//        for (IData data : inputs) {
//            final String entityId = data.getEntityId();
//            if (!toReturn.containsKey(entityId)) {
//                toReturn.put(entityId, new HashMap<String, IData>());
//            }
//            toReturn.get(entityId).put(data.getInputName(), data);
//        }
//        return toReturn;
//    }
    
    /**
     * This method create entities from the input data
     * @param inputs input data
     * @return the entities ready to be notified
     */
//    public Collection<AbstractConfiguredEntityStatusesType> createEntities(final Map<String, Map<String, IData>> inputs) {
//        final Map<String, AbstractConfiguredEntityStatusesType> toReturn = 
//                new LinkedHashMap<String, AbstractConfiguredEntityStatusesType>();
//        for (Entry<String, Map<String, IData>> entry : inputs.entrySet()) {
//            final String entityId = entry.getKey();
//            if (!toReturn.containsKey(entityId)) {
//                try {
//                    LOGGER.trace("creating entity notification for [{}]", entityId);
//                    toReturn.put(entityId, builder_.getNotificationEntity(entityId));
//                } catch (HypervisorConversionException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//            fillEntity(toReturn.get(entityId), entry.getValue());
//        }
//        LOGGER.debug("[{}] entities has been filled", toReturn.size());
//        return toReturn.values();
//    }
    
    public IData getScsValue(final AbstractConfiguredEntityStatusesType entity, final String attributeName) {
    	IData toReturn = null;
    	
    	String entityId = entity.getId();
    	
    	try {
			AbstractAttributeType att = dataHelper_.getAttribute(entity, attributeName);
			IData inputHvData = (IData) new HvDataWrapper(entityId, attributeName, att);
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
    	
    	//List<String>inputNames = bindingLoader_.getInputNames(entityType, attributeName);
    	
    	return toReturn;
    }
    
    public IData getMappedScsValue(final AbstractConfiguredEntityStatusesType entity, final String inputName) throws HypervisorException {
    	IData toReturn = null;
    	
    	String entityId = entity.getId();
    	HashSet<AttributeBinding> bindingSet = new HashSet<AttributeBinding>(bindingLoader_.getDescription(entityId, inputName));
    	if (bindingSet.isEmpty()) {
			throw new HypervisorException("Unable to get binding for entity " + entityId);
		}
		// Assume only 1 binding that matches. Use the first binding.
		AttributeBinding binding = bindingSet.iterator().next();

		IData inputHvData = (IData) new HvDataWrapper(entityId, inputName, null);
		Map<String, IData>dataMap = new HashMap<String, IData>();
		dataMap.put(inputName, inputHvData);
		
		toReturn = getMergedData(entity, (MultiInputBinding) binding, dataMap);
    	
    	return toReturn;
    }
    
    public Collection<AttributeBinding> getAttributeBindings(String bindingId, String hvInputAttribute) {
    	return bindingLoader_.getAttributeBindings(new CompositeKey(bindingId, hvInputAttribute));
    }

	public String getScsValueType(AttributeBinding binding) {
		if (binding instanceof StringToIntegerBinding) {
			return "Integer";
		} else if (binding instanceof BooleanToIntegerBinding) {
			return "Integer";
		} else if (binding instanceof BooleanBinding) {
			return "Boolean";
		} else if (binding instanceof IntegerEnumerationRangeBinding) {
			return "Integer";
		} else if (binding instanceof StringEnumerationRangeBinding) {
			return "String";
		}
		return null;
	}

    /**
     * fill the content of the entity from the data
     * @param entity the entity to fill
     * @param data the data to use
     */
//    private void fillEntity(AbstractConfiguredEntityStatusesType entity, 
//                            final Map<String, IData> dataMap) {
//        if (LOGGER.isTraceEnabled()) {
//            LOGGER.trace("Filling entity [{}] for inputs {}", entity, dataMap.keySet());
//        }
//        for (IData data : dataMap.values()) {
//            //find the binding descripton for each data (could be multiple if one data is used
//            // to fill multiple attributes)
//            final Collection<AttributeBinding> descriptions = bindingLoader_.getDescription(data);
//            for (AttributeBinding description : descriptions) {
//                addAttribute(entity, description, dataMap);
//            }
//        }
//        
//    }

    /**
     * add an attribute in the entity from the data map
     * @param entity entity to fill
     * @param description binding description to know the rule
     * @param dataMap map of data (to handle multiple binding)
     */
//    private void addAttribute(AbstractConfiguredEntityStatusesType entity, final AttributeBinding description, 
//            final Map<String, IData> dataMap) {
//        
//        final IData convertedData = (description instanceof MonoInputBinding) 
//                ? getMergedDate((MonoInputBinding) description, dataMap) 
//                : getMergedDate(entity, (MultiInputBinding) description, dataMap);
//        
//        if (convertedData != null) {
//            final AbstractAttributeType attribute = attBuilder_.createAttribute(
//                    entity.getClass(), description.getId(), convertedData);
//            if (attribute != null) {
//                try {
//                    dataHelper_.setAttribute(entity, description.getId(), attribute);
//                } catch (EntityManipulationException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//        } else {
//            LOGGER.debug("No converted value is output for data [{}]", dataMap);
//        }
//    }
    
    /**
     * convert the mono input 
     * @param description binding description
     * @param dataMap dataMap to use (in fact only one entry will be used based on the desciption)
     * @return the converted data
     */
//    private IData getMergedDate(final MonoInputBinding description, Map<String, IData> dataMap) {
//        return bindingLoader_.getConverterManager().convert(description, dataMap.get(description.getInput().getName()));
//    }
    
    /**
     * convert the multiple input 
     * @param entity entity to use
     * @param description binding description
     * @param dataMap dataMap to use (first merge, then convert)
     * @return the converted data
     */
    private IData getMergedData(AbstractConfiguredEntityStatusesType entity, final MultiInputBinding description, 
            final Map<String, IData> dataMap) {
        
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
    
    
    /**
     * get the list of the input names from alias and attributes
     * @param alias name of the binding alias
     * @param attribute name of the hypervisor attribute
     * @return the list of the nneded input names
     */
//    public final Collection<String> getInputNames(final String alias, final String attribute) {
//        return bindingLoader_.getInputNames(alias, attribute);
//    }
   
}
