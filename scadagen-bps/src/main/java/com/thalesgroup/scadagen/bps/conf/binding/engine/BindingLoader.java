package com.thalesgroup.scadagen.bps.conf.binding.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.configuration.NotificationEntityBuilder;
import com.thalesgroup.hv.common.tools.MarshallersPool;
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data_v1.entity.AbstractConfiguredEntityStatusesType;
import com.thalesgroup.hv.data_v1.entity.configuration.AbstractConfiguredEntityType;
import com.thalesgroup.hv.data_v1.equipment.configuration.AbstractEquipmentType;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.scadagen.binding.AttributeBinding;
import com.thalesgroup.scadagen.binding.ClassBinding;
import com.thalesgroup.scadagen.binding.Configuration;
import com.thalesgroup.scadagen.binding.Input;
import com.thalesgroup.scadagen.binding.MonoInputBinding;
import com.thalesgroup.scadagen.binding.MultiInputBinding;
import com.thalesgroup.scadagen.binding.Type;
import com.thalesgroup.scadagen.bps.conf.binding.computer.ComputerManager;
import com.thalesgroup.scadagen.bps.conf.binding.converter.ConverterManager;
import com.thalesgroup.scadagen.bps.conf.binding.data.IData;

/**
 * Binding description loader
 */
public class BindingLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(BindingLoader.class);

    /** location of the xsd */
    private static final String xsdLocation = "xsd/bindingHv2Scs.xsd";
    
    
    /** marshaller to read the configuration */
    private final MarshallersPool marshallers_;

    /** index of the attribute binding description to improve performance when converting data 
     * The first part of the index is the name of the alias
     * The second part of the index is the name of the input
     */
    private final HashMultimap<CompositeKey, AttributeBinding> indexForInputConversion_ = 
            HashMultimap.create();
    
    /** index of the attribute binding description to improve performance when converting data 
     * The first part of the index is the name of the alias
     * The second part of the index is the name of the attribute
     */
    private final HashMap<CompositeKey, AttributeBinding> indexForBindingNavigation_ = 
            new HashMap<CompositeKey, AttributeBinding>();
    
    /** computer manager to register the computers */
    private final ComputerManager computerManager_ = new ComputerManager();

    /** computer manager to register the converters */
    private final ConverterManager converterManager_;
    
    /** reference to the connector to see the system configuration */
    private final Connector connector_;
    
    /**
     * Constructor
     * @param connector connector to access the system configuration and other tools
     * @throws HypervisorException unable to initialize the BindingLoader
     */
    public BindingLoader(final Connector connector) throws HypervisorException {
        this(connector, new ConverterManager());
    }
    
    /**
     * Constructor
     * @param connector connector to access the system configuration and other tools
     * @param converterManager converter manager to be able to provide custom converters
     * @throws HypervisorException unable to initialize the BindingLoader
     */
    public BindingLoader(final Connector connector,
            final ConverterManager converterManager) throws HypervisorException {
        marshallers_ = new MarshallersPool(Configuration.class.getPackage().getName(), xsdLocation);
        converterManager_ = converterManager;
        connector_ = connector;
    }
    
    /**
     * read the default configuration file to generate the in memory configuration
     * @throws HypervisorConversionException unable to parse this configuration
     */
    public void readBinding() throws HypervisorConversionException {
        readBinding("binding.xml");
    }
    /**
     * read the configuration file to generate the in memory configuration
     * @param fileName file to parse
     * @throws HypervisorConversionException unable to parse this configuration
     */
    public void readBinding(final String fileName) throws HypervisorConversionException {
        parseConfiguration(marshallers_.<Configuration>unmarshal(fileName));
    }

    /**
     * parse the configuration to prepare all the needed tools
     * @param configuration JAXB configuration
     */
    public void parseConfiguration(final Configuration configuration) {
        
        // get the resolution alias => notification class to be able to enfore automatically
        // the input type declaration
        final Map<String, Class<? extends AbstractConfiguredEntityStatusesType>> aliasSupport = 
                getAliasClassMapping();
        
        //a list to be able to manage a beginning of inheritance
        final List<ClassBinding> needSuper = new ArrayList<ClassBinding>();
        //the class binding index also for inheritance support
        final Map<String, ClassBinding> classBindingIndex = new HashMap<String, ClassBinding>();
        
        for (ClassBinding classBinding : configuration.getBinding()) {
            //register the class in the local index
            classBindingIndex.put(classBinding.getId(), classBinding);
            for (AttributeBinding attBinding : classBinding.getAttribute()) {
                if (attBinding instanceof MonoInputBinding) {
                    //mono input use case
                    final MonoInputBinding monoInputBinding = (MonoInputBinding) attBinding;
                    Input input = monoInputBinding.getInput();
                    if (input == null) {
                        //input not defined enforce to the same name as the attribute binding identifier
                        input = createDefaultInput(monoInputBinding);
                        monoInputBinding.setInput(input);
                    }
                    if (input.getType() == null) {
                        //type not defined try to enforce the value
                        final Class<? extends AbstractConfiguredEntityStatusesType> targetClass = 
                            aliasSupport.get(classBinding.getId());
                        try {
                            //in order to help other steps we enforce the type declaration of the input
                            // by using the model
                            final Class<?> type = connector_.getDataHelper().getMemberType(targetClass, attBinding.getId());
                            enforceTypeDeclaration(input, type);
                        } catch (EntityManipulationException e) {
                            LOGGER.error("attribute [{}] does not exist for binding [{}]", 
                                    attBinding.getId(), classBinding.getId());
                            input.setType(Type.INT);
                        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                            LOGGER.error("Array out of bound [{}] [{}] ", targetClass.getName(), attBinding.getId());
                        }
                    }
                    //register the converter
                    converterManager_.add(attBinding);
                    registerAttBindingInConversion(classBinding, monoInputBinding);
                    
                } else if (attBinding instanceof MultiInputBinding) {
                    final MultiInputBinding multiInputBinding = (MultiInputBinding) attBinding;
                    final List<Input> inputs = multiInputBinding.getInputs();
                    if (inputs.size() > 0) {
                        //we have to loop other the inputs in order to register correctly the binding
                        for (int i = 0; i < inputs.size(); i++) {
                            final String inputName = inputs.get(i).getName();
                            indexForInputConversion_.put(new CompositeKey(classBinding.getId(), inputName), attBinding);
                        }
                    } else {
                        //this one should not happen
                        indexForInputConversion_.put(new CompositeKey(classBinding.getId(), attBinding.getId()), attBinding);
                    }
                    //register the converter
                    converterManager_.add(attBinding);
                    //register the computer
                    computerManager_.add(multiInputBinding);
                    
                } else {
                    LOGGER.warn("The current implementation does not support [{}]", attBinding.getClass().getName());
                }
                indexForBindingNavigation_.put(new CompositeKey(classBinding.getId(), attBinding.getId()), attBinding);
                LOGGER.debug("Binding Key=Composite of {},{}", classBinding.getId(), attBinding.getId());
            }
            //if there is a super definition wait 
            if (classBinding.getSuper() != null) {
                needSuper.add(classBinding);
            }
        }

        //FIXME : naive inheritance implementation
        // should be process if no more dependency needed otherwise add in toProcesslist.
        // at the end loop over for those which are now resolved and so on.
        // add a check that at least one resolution has been performed per loop to avoid infinite looping
        for (final ClassBinding classBinding : needSuper) {
            final ClassBinding parentBinding = classBindingIndex.get(classBinding.getSuper());
            for (final AttributeBinding parentAttributeBinding : parentBinding.getAttribute()) {
                //the lookup key in the child
                final CompositeKey key = new CompositeKey(classBinding.getId(), parentAttributeBinding.getId());
                if (!indexForBindingNavigation_.containsKey(key)) {
                    //the child does not contains a overriding binding for this attribute add it
                    classBinding.getAttribute().add(parentAttributeBinding);
                    if (parentAttributeBinding instanceof MonoInputBinding) {
                        registerAttBindingInConversion(classBinding, (MonoInputBinding) parentAttributeBinding);
                    }
                    indexForBindingNavigation_.put(key, parentAttributeBinding);
                }
            }
        }
    }

    /**
     * enforce the declaration of the type in the type declaration of the input
     * @param input input to enforce
     * @param type the target type (as java class)
     * @return the input with enforced type declaration
     */
    private void enforceTypeDeclaration(Input input, final Class<?> type) {
        if (Integer.TYPE.equals(type)) {
            input.setType(Type.INT);
        } else if (Long.TYPE.equals(type)) {
            input.setType(Type.LONG);
        } else if (Float.TYPE.equals(type)) {
            input.setType(Type.FLOAT);
        } else if (Double.TYPE.equals(type)) {
            input.setType(Type.DOUBLE);
        } else if (Boolean.TYPE.equals(type)) {
            input.setType(Type.BOOL);
        } else if (String.class.equals(type)) {
            input.setType(Type.STRING);
        } else if (Date.class.equals(type)) {
            input.setType(Type.DATE);
        } else {
            input.setType(Type.INT);
        }
    }
    
    /**
     * register an attribute binding in the conversion index
     * @param classBinding classbinding to use
     * @param binding attribute binding to register
     */
    private void registerAttBindingInConversion(final ClassBinding classBinding, final MonoInputBinding binding) {
        final CompositeKey key = new CompositeKey(classBinding.getId(), binding.getInput().getName());
        indexForInputConversion_.put(key, binding);
    }

    /**
     * convenient method to find from the system configuration and alias resolution the
     * expected notification class for a configuration alias
     * @return the mapping alias notification class
     */
    private Map<String, Class<? extends AbstractConfiguredEntityStatusesType>> getAliasClassMapping() {
        final Map<String, Class<? extends AbstractConfiguredEntityStatusesType>> aliasSupport = 
                new HashMap<String, Class<? extends AbstractConfiguredEntityStatusesType>>();
        //final Set<AbstractConfiguredEntityType> entities = connector_.getSystemConfiguration().getOwnedEntities();
        final Set<AbstractConfiguredEntityType> entities = new HashSet<AbstractConfiguredEntityType> (connector_.getSystemConfiguration().getEntitiesAsMap().values());
        final NotificationEntityBuilder builder = new NotificationEntityBuilder(
                connector_.getSystemConfiguration(), connector_.getDataHelper());

        //a small cache to avoid to find the notification class each time
        for (final AbstractConfiguredEntityType entity : entities) {
        	//skip all AbstractSubSystemType
        	if (!(entity instanceof AbstractEquipmentType)) {
        		continue;
        	}
            //try to find the alias to use for this entity
            final String alias = resolveBinding(entity.getId());
            if (alias == null) {
                LOGGER.debug("Entity [{}] is not managed by the binding mechanism.", entity.getId());
            } else {
                if (!aliasSupport.containsKey(alias)) {
                    //not yet determined
                    try {
                        //find the notification class
                        aliasSupport.put(alias, builder.getNotificationEntityClass(entity));
                    } catch (HypervisorConversionException e) {
                        LOGGER.warn("Unable to find a notification class for entity [{}]. "
                                + "The binding [{}] will fail", entity.getId(), alias);
                    }
                }
            }
        }
        return aliasSupport;
    }

    /**
     * create a default input in for the given attribute
     * @param attributeBinding attribute to used
     * @return the created input
     */
    private Input createDefaultInput(AttributeBinding attributeBinding) {
        final Input input = new Input();
        input.setName(attributeBinding.getId());
        return input;
    }
    
    /**
     * get the descriptions from the data
     * @param data the data to analyze
     * @return the attribute bindings which shall be performed for this data
     */
    public final Collection<AttributeBinding> getDescription(final IData data) {
        final Collection<AttributeBinding> value = indexForInputConversion_.get(
            new CompositeKey(resolveBinding(data.getEntityId()), data.getInputName()));
        if (value.isEmpty()) {
            LOGGER.warn("Unable to find a binding for data [{}, {}]", data.getEntityId(), data.getInputName());
        }
        return value;
    }
    
    /**
     * get the descriptions from the data
     * @param data the data to analyze
     * @return the attribute bindings which shall be performed for this data
     */
    public final Collection<AttributeBinding> getDescription(final String entityId, final String inputName) {
        final Collection<AttributeBinding> value = indexForInputConversion_.get(
            new CompositeKey(resolveBinding(entityId), inputName));
        if (value.isEmpty()) {
            LOGGER.warn("Unable to find a binding for data [{}, {}]", entityId, inputName);
        }
        return value;
    }
    
    /**
     * get the list of the input names from alias and attributes
     * @param alias name of the binding alias
     * @param attribute name of the hypervisor attribute
     */
    public final Collection<String> getInputNames(final String alias, final String attribute) {
        final List<String> inputNames = new ArrayList<String>();
        final AttributeBinding att = indexForBindingNavigation_.get(new CompositeKey(alias, attribute));
        //here we are able to simply read the input as they have been prepared during the read of the configuration
        if (att instanceof MonoInputBinding) {
            inputNames.add(((MonoInputBinding) att).getInput().getName());
        } else if (att instanceof MultiInputBinding) {
            for (final Input input : ((MultiInputBinding) att).getInputs()) {
                inputNames.add(input.getName());
            }
        }
        return inputNames;
    }
    
    /**
     * get the computer manager
     * @return the computer manager
     */
    public ComputerManager getComputerManager() {
        return computerManager_;
    }

    /**
     * get the converter manager
     * @return the converter manager
     */
    public ConverterManager getConverterManager() {
        return converterManager_;
    }
    
    /**
     * find the binding to use from the entity identifier
     * @param entityIdentifier entity identifier
     * @return the binding identifier defined for this entity
     */
    protected String resolveBinding(final String entityIdentifier) {
        return entityIdentifier;
    }

	public Collection<AttributeBinding> getAttributeBindings(CompositeKey compositeKey) {
		return indexForInputConversion_.get(compositeKey);
	}
}
