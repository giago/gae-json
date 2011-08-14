package com.giago.clag.converter.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.giago.clag.converter.Converter;
import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;
import com.giago.clag.servlet.context.Context;
import com.giago.clag.servlet.context.GaeRestContext;
import com.giago.clag.servlet.context.ServiceInfo;

/**
 * @author luigi.agosti
 */
public class CsvConverterTest extends AbstractConverterTest {

	@Override
	protected Converter initConverter() {
		return new CsvConverter();
	}
	
	@Test
	public void convertMetaDataSet() {
		String result = converter.convert(getSampleEntity(), context);

		assertEquals("", result);
	}

	@Test
	public void convertCursor() {
		Cursor cursor = new Cursor();
		cursor.add("title", "title value");
		cursor.add("description", "description value");
		cursor.add("cost", 1);
		cursor.add("id", 1);
		cursor.next();

		String result = converter.convert(cursor, getSampleEntity(), context);

		assertEquals("title value,description value,1,1\n", result);
	}

	@Test
	public void convertCursorWithMoreThanOne() {
		Cursor cursor = new Cursor();
		cursor.add("title", "title value");
		cursor.add("description", "description value");
		cursor.add("cost", 1);
		cursor.add("id", 1);
		cursor.next();
		cursor.add("id", 2);
		cursor.add("title", "title value2");
		cursor.add("description", "description value2");
		cursor.add("cost", 12);
		cursor.next();

		String result = converter.convert(cursor, getSampleEntity(), context);

		assertEquals(
				"title value,description value,1,1\n" + 
				"2,title value2,description value2,12\n",
				result);
	}

	@Test
	public void convertCursorWithoutObject() {
		String result = converter.convert(new Cursor(), getSampleEntity(),
				context);

		assertEquals("", result);
	}

	@Test
	public void describeWithNullContext() {
		String result = converter.describe(null);
		assertEquals("", result);
	}

	@Test
	public void describe() {
		Context context = new GaeRestContext();
		context.setProvider(getSampleProvider());
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setName("testApplication");
		serviceInfo.setVersion("1");
		context.setServiceInfo(serviceInfo);
		String result = converter.describe(context);
		assertEquals("", result);
	}

	@Test
	public void describeWithNoProvider() {
		Context context = new GaeRestContext();
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setName("testApplication");
		serviceInfo.setVersion("1");
		context.setServiceInfo(serviceInfo);
		String result = converter.describe(context);
		assertEquals("", result);
	}

	@Test
	public void convertMetaDataSetWithReplaceUnique() {
		String result = converter.convert(getComplexEntity(), context);

		assertEquals("", result);
	}
	
	@Test
	public void convertCursorWithBooleanValue() {
		Cursor cursor = new Cursor();
		cursor.add("title", "title value");
		cursor.add("description", "description value");
		cursor.add("cost", 1);
		cursor.add("id", 1);
		cursor.add("enabled", Boolean.TRUE);
		cursor.next();

		MetaEntity me = getSampleEntity();
		me.add(new MetaProperty.Builder("enabled").clazz(Boolean.class).build());
		String result = converter.convert(cursor, me, context);
		
		assertEquals("title value,description value,1,1,true\n", result);
	}
}
