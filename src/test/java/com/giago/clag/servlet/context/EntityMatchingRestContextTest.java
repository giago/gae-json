package com.giago.clag.servlet.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;


import org.junit.Ignore;
import org.junit.Test;

import com.giago.clag.model.MetaEntity;
import com.giago.clag.util.RequestMapBuilder;

public class EntityMatchingRestContextTest {

	@Test
	public void shouldGetEntityPropertiesReturnMapWithTextValue() {
		Map<String, Object> properties = entityMatchingText("key",
				String.class, "should be ok", String.class);

		assertProperty("id", "key", properties);
		assertProperty("second", "should be ok", properties);
	}

	/**
	 * TODO when the real insert is implemented in the provider I can check if
	 * the context is the best place to convert the type
	 */

	@Ignore
	@Test
	public void shouldGetEntityPropertiesReturnMapWithLong() {
		Map<String, Object> properties = entityMatchingText("key",
				String.class, "12", Integer.class);

		assertProperty("id", "key", properties);
		assertProperty("second", "should be ok", properties);
	}

	@Ignore
	@Test
	public void shouldGetEntityPropertiesReturnMapWithDate() {
		Map<String, Object> properties = entityMatchingText("key",
				String.class, "122938420", String.class);

		assertProperty("id", "key", properties);
		assertProperty("second", "should be ok", properties);
	}

	private void assertProperty(String name, Object expected,
			Map<String, Object> properties) {
		assertTrue(properties.containsKey(name));
		assertEquals(expected, properties.get(name));
	}

	private Map<String, Object> entityMatchingText(String fristProperty,
			Class<?> firstClass, String secondProperty, Class<?> secondClass) {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add(
				"id", fristProperty).add("second", secondProperty).build());

		rr.setUri("/data/Story");

		MetaEntity me = new MetaEntity("Example", "Example");
		me.add("id", firstClass);
		me.add("second", secondClass);

		Map<String, Object> properties = rr.getEntity(me);
		assertNotNull(properties);
		return properties;
	}

}
