package com.thalesgroup.rtt.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.HypervisorInitializationError;
import com.thalesgroup.hv.common.tools.XMLTool;
import com.thalesgroup.hv.data.exception.EntityManipulationException;
import com.thalesgroup.hv.data.tools.bean.BeanEditor;
import com.thalesgroup.hv.data.tools.bean.BeanManipulationException;
import com.thalesgroup.hv.data.tools.bean.IBeanEditor;
import com.thalesgroup.hv.data.tools.finder.FinderSpringImpl;
import com.thalesgroup.hv.data.tools.finder.IFinder;
import com.thalesgroup.hv.data.tools.helper.AttributeHelper;
import com.thalesgroup.hv.data.tools.helper.DOMConverterHelper;
import com.thalesgroup.hv.data.tools.helper.IDOMConverterHelper;
import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data.tools.helper.ModelConstants;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeSimpleType;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;

public class EntityDataHelper implements IDataHelper {
	private static final Class<AbstractEntityStatusesType> ENTITY_CLASS = AbstractEntityStatusesType.class;

	private BeanEditor editor_;

	private final Map<QName, Class<? extends AbstractEntityStatusesType>> mapping_ = new HashMap();

	private final Map<Class<? extends AbstractEntityStatusesType>, QName> reverseMapping_ = new HashMap();

	private final Set<String> packageNames_ = new HashSet();

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityDataHelper.class);

	private IDOMConverterHelper DOMHelper_;

	public EntityDataHelper()
	  {
	    this(true);
	  }

	public EntityDataHelper(boolean initialize)
	  {
	    if (initialize) {
	      initialize();
	    }
	  }

	public void initialize() throws HypervisorInitializationError {
		long startGeneration = System.currentTimeMillis();
		LOGGER.info("Initializing helper...");

		IFinder finder = new FinderSpringImpl();
		Set<String> entityStatusesTypeNames = finder.findClasses(ModelConstants.BASE_PACKAGES, ENTITY_CLASS);

		ClassLoader classLoader = ENTITY_CLASS.getClassLoader();

		List<Exception> errors = new ArrayList();
		for (String entityStatusesTypeName : entityStatusesTypeNames) {
			try {
				if (AbstractEntityStatusesType.class.isAssignableFrom(Class.forName(entityStatusesTypeName))) {
					Class<? extends AbstractEntityStatusesType> clazz = (Class<? extends AbstractEntityStatusesType>) classLoader.loadClass(entityStatusesTypeName);
	
					QName qName = XMLTool.getQNameForType(clazz);
	
					mapping_.put(qName, clazz);
					reverseMapping_.put(clazz, qName);
					packageNames_.add(clazz.getPackage().getName());
				}
			} catch (Exception e) {
				errors.add(e);
			}
		}

		if (!errors.isEmpty()) {
			StringBuilder errorMessage = new StringBuilder(500);
			errorMessage.append("Unable to load all the model entity classes : ");
			for (Exception error : errors) {
				errorMessage.append("[").append(error.getMessage()).append("] ");
			}
			throw new HypervisorInitializationError(errorMessage.toString());
		}
		try {
			editor_ = new BeanEditor(new HashSet(reverseMapping_.keySet()));

			DOMHelper_ = new DOMConverterHelper(getManagedPackages());
		} catch (HypervisorException e) {
			throw new HypervisorInitializationError("Error while initializing the EntityHelper DOM Converter", e);
		}
		LOGGER.debug("Helper initialized in {}ms.", Long.valueOf(System.currentTimeMillis() - startGeneration));
	}

	public void reset() throws HypervisorInitializationError {
		LOGGER.info("Reseting Entity helper.");
		initialize();
		LOGGER.info("Reset done.");
	}

	public Set<String> getDeclaredStatusNames(Class<? extends AbstractEntityStatusesType> entityType)
			throws EntityManipulationException {
		Set<String> fieldNames = editor_.getFieldNames(entityType);

		fieldNames.remove("id");
		return fieldNames;
	}

	public Set<String> getDeclaredStatusNames(String entityTypeName) throws EntityManipulationException {
		Set<String> fieldNames = editor_.getFieldNames(entityTypeName);

		fieldNames.remove("id");
		return fieldNames;
	}

	public Object getAttributeValue(AbstractEntityStatusesType entity, String attributeCanonicalName)
			throws EntityManipulationException {
		if ((attributeCanonicalName == null) || ("".equals(attributeCanonicalName)) || (entity == null)) {
			throw new EntityManipulationException("Error while getting attribute: name or entity is null.");
		}

		AbstractAttributeType attribute = getAttribute(entity, attributeCanonicalName);

		if ((attribute instanceof AbstractAttributeSimpleType)) {
			try {
				return getAttributeValue((AbstractAttributeSimpleType) attribute);

			} catch (EntityManipulationException e) {
				throw new EntityManipulationException("Cannot get value of the attribute [" + attributeCanonicalName
						+ "] from the entity [" + entity.getId() + "] of type [" + entity.getClass().getSimpleName()
						+ "]." + " The type [" + attribute.getClass().getSimpleName() + "] is no managed.");
			}
		}

		throw new EntityManipulationException("Cannot get value for a complex attribute [" + attributeCanonicalName
				+ "] from the entity [" + entity.getId() + "] of type [" + entity.getClass().getSimpleName() + "].");
	}

	public Object getAttributeValue(AbstractAttributeSimpleType attribute) throws EntityManipulationException {
		return AttributeHelper.getAttributeValue(attribute);
	}

	public Class<?> getAttributeValueType(AbstractEntityStatusesType entity, String attributeCanonicalName)
			throws EntityManipulationException {
		if ((attributeCanonicalName == null) || ("".equals(attributeCanonicalName)) || (entity == null)) {
			throw new EntityManipulationException("Error while getting attribute: name or entity is null.");
		}
		return getAttributeValue(entity, attributeCanonicalName).getClass();
	}

	public Class<?> getMemberType(Class<? extends AbstractEntityStatusesType> clazz, String memberCanonicalName)
			throws EntityManipulationException {
		try {
			return editor_.getType(clazz, memberCanonicalName);
		} catch (BeanManipulationException e) {
			throw new EntityManipulationException(e.getMessage());
		}
	}

	public void setAttributeValue(AbstractEntityStatusesType entity, String attributeCanonicalName, Object value,
			long date) throws EntityManipulationException {
		AbstractAttributeType attribute = getAttribute(entity, attributeCanonicalName);

		if (attribute != null) {
			attribute.setTimestamp(date);
		} else {
			throw new EntityManipulationException("Cannot set value of the attribute [" + attributeCanonicalName
					+ "] from the entity [" + entity.getId() + "] of type [" + entity.getClass().getSimpleName() + "]."
					+ " The attribute is not defined in the declared type.");
		}

		AttributeHelper.setAttributeValue(entity, attributeCanonicalName, value, attribute);
	}

	public AbstractAttributeType getAttribute(AbstractEntityStatusesType entity, String attributeCanonicalName)
			throws EntityManipulationException {
		if ((attributeCanonicalName == null) || ("".equals(attributeCanonicalName)) || (entity == null)) {
			throw new EntityManipulationException("Error while getting attribute: name is null or empty.");
		}
		try {
			Object currentObject = editor_.getValue(entity, attributeCanonicalName);
			if (((currentObject instanceof AbstractAttributeType)) || (currentObject == null)) {
				return (AbstractAttributeType) currentObject;
			}
			throw new EntityManipulationException("attribute [" + attributeCanonicalName + "] for entity ["
					+ entity.getId() + "] is not valid. There is an error in the model definition." + "The type "
					+ currentObject.getClass().getName() + "is not yet supported");

		} catch (BeanManipulationException e) {

			throw new EntityManipulationException(e.getMessage());
		}
	}

	public Map<String, AbstractAttributeType> getAttributes(AbstractEntityStatusesType entity,
			Collection<String> attributeCanonicalNames) throws EntityManipulationException {
		if ((attributeCanonicalNames == null) || (entity == null)) {
			throw new EntityManipulationException("Error while getting attribute: name is null or empty.");
		}
		Map<String, AbstractAttributeType> toReturn = new HashMap(attributeCanonicalNames.size());

		for (String attributeName : attributeCanonicalNames) {
			toReturn.put(attributeName, getAttribute(entity, attributeName));
		}

		return toReturn;
	}

	public void setAttribute(AbstractEntityStatusesType entity, String attributeCanonicalName,
			AbstractAttributeType newAttribute) throws EntityManipulationException {
		try {
			editor_.setValue(entity, attributeCanonicalName, newAttribute);
		} catch (Exception e) {
			throw new EntityManipulationException(
					"Cannot set the new value for the attribute [" + attributeCanonicalName + "] of the entity ["
							+ entity.getId() + "] of type [" + entity.getClass().getSimpleName() + "].",
					e);
		}
	}

	public Set<String> getManagedPackages() {
		return new HashSet(packageNames_);
	}

	public IDOMConverterHelper getDOMConverterHelper() {
		return DOMHelper_;
	}

	public Class<? extends AbstractEntityStatusesType> getClassFromQName(QName qName) {
		return (Class) mapping_.get(qName);
	}

	public IBeanEditor getBeanEditor() {
		return editor_;
	}
}
