package profundo.pushparser;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class BeanWrapper {
	private BeanInfo beanInfo;
	private Map<String, PropertyDescriptor> propertyDescriptors = new HashMap<String, PropertyDescriptor>();
	private Map<String, Field> fields = new HashMap<String, Field>();
	

	public BeanWrapper(Class<?> type) {
		try {
			this.beanInfo = Introspector.getBeanInfo(type);
			for (PropertyDescriptor p : beanInfo.getPropertyDescriptors()) {
				propertyDescriptors.put(p.getName(), p);
			}
			for (Field p : type.getFields()) {
				if (Modifier.isStatic(p.getModifiers())) continue;
				fields.put(p.getName(), p);
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}

	public void set(Object self, String key, Object value) {
		try {
			PropertyDescriptor pd = propertyDescriptors.get(key);
			if (pd == null) {
				Field f = fields.get(key);
				if (f == null)
					throw new NoSuchFieldException(key);
				f.set(self, coerce(f.getType(), value));
			} else {
				pd.getWriteMethod().invoke(self, coerce(pd.getPropertyType(), value));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T coerce(Class<T> propertyType, Object value) {
		if (propertyType == value.getClass()) {
			return (T) value;
		} else if (propertyType == Integer.TYPE) {
			return (T) new Integer(String.valueOf(value));
		} else {
			return (T) value;
		}
	}
}
