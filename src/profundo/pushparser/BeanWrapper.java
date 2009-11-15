package profundo.pushparser;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/*
 * 
 * Copyright 2009 Erik Martino Hansen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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
