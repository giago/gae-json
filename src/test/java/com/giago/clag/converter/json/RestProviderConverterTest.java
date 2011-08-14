package com.giago.clag.converter.json;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;


import org.junit.Test;

import com.giago.clag.converter.Converter;
import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.provider.Provider;
import com.giago.clag.provider.gae.GaeProvider;
import com.giago.clag.servlet.context.Context;
import com.giago.clag.servlet.context.GaeRestContext;
import com.giago.clag.servlet.context.ServiceInfo;

/**
 * @author luigi.agosti
 */
public class RestProviderConverterTest extends AbstractConverterTest {

	private static final String EXAMPLE_JSON = "{\"name\":\"Example\",\"columns\":["
		+ "{\"name\":\"id\",\"type\":\"integer\",\"key\":\"true\"},"
		+ "{\"name\":\"title\",\"type\":\"text\"}"
		+ ",{\"name\":\"description\",\"type\":\"text\"}"
		+ ",{\"name\":\"cost\",\"type\":\"integer\"}],\"children\":[]}";

	@Override
	protected Converter initConverter() {
		return new RestProviderConverter();
	}
	
	@Test
	public void convertMetaDataSet() {
		String result = converter.convert(getSampleEntity(), context);

		assertEquals(EXAMPLE_JSON, result);
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

		assertEquals(
				"[{\"title\":\"title value\",\"description\":\"description value\",\"cost\":1,\"id\":1}]",
				result);
	}

	@Test
	public void convertCursorWithDateField() {
		Cursor cursor = new Cursor();
		cursor.add("date", new Date(1));
		cursor.next();

		String result = converter.convert(cursor, getSampleEntity(), context);

		assertEquals("[{\"date\":1}]", result);
	}

	@Test
	public void convertCursorWithListOfString() {
		Cursor cursor = new Cursor();
		cursor.add("groupIds", Arrays.asList("1", "2"));
		cursor.next();
		
		String result = converter.convert(cursor, getSampleEntity(), context);
		
		assertEquals("[{\"groupIds\":[\"1\",\"2\"]}]", result);
	}

	@Test
	public void convertCursorWithMoreThanOne() {
		Cursor cursor = new Cursor();
		cursor.add("title", "title value");
		cursor.add("description", "description value");
		cursor.add("cost", 1);
		cursor.add("id", 1);
		cursor.next();
		cursor.add("title", "title value2");
		cursor.add("description", "description value2");
		cursor.add("cost", 12);
		cursor.add("id", 2);
		cursor.next();

		String result = converter.convert(cursor, getSampleEntity(), context);

		assertEquals(
				"[{\"title\":\"title value\",\"description\":\"description value\",\"cost\":1,\"id\":1},"
						+ "{\"id\":2,\"title\":\"title value2\",\"description\":\"description value2\",\"cost\":12}]",
				result);
	}
	
	@Test
	public void convertCursorWithSubCursor() {
		Cursor cursor = new Cursor();
		cursor.add("title", "cursor");
		Cursor subCursor = new Cursor("Subcursor");
		subCursor.add("title", "subCursor");
		subCursor.next();
		cursor.add("Subcursor", subCursor);
		cursor.next();
		
		String result = converter.convert(cursor, null, context);
		
		assertEquals("[{\"title\":\"cursor\",\"Subcursor\":[{\"title\":\"subCursor\"}]}]", result);
	}

	@Test
	public void convertCursorWithoutObject() {
		String result = converter.convert(new Cursor(), getSampleEntity(), context);

		assertEquals("[]", result);
	}
	
	@Test
	public void describeWithNullContext() {
		String result = converter.describe(null);
		assertEquals("{}", result);
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
		assertEquals(
			"{\"name\":\"testApplication\",\"version\":\"1\"," +
				"\"services\":[" +
					"{\"get\":[\"Example\"]}" +
				"]," +
				"\"schema\":[" +
					EXAMPLE_JSON +
				"]" +
			"}", result);
	}
	
	@Test
	public void convertWithSelfRelationWithTheOwnerOfTheRelation() {
		Context context = new GaeRestContext();
		MetaEntity me = getSampleEntity();
		me.addRelation("parentId", "Example", "Example1", String.class, true);
		Provider provider = new GaeProvider();
		provider.add(me);
		context.setProvider(provider);
		
		String result = converter.convert(me, context);
		assertEquals(
			"{\"name\":\"Example\",\"columns\":[{\"name\":\"id\",\"type\":\"integer\",\"key\":\"true\"}," +
				"{\"name\":\"parentId\",\"type\":\"text\"},{\"name\":\"title\",\"type\":\"text\"}," +
				"{\"name\":\"description\",\"type\":\"text\"},{\"name\":\"cost\",\"type\":\"integer\"}]," +
					"\"children\":[{\"name\":\"Example\",\"columns\":[{\"name\":\"id\",\"type\":\"integer\",\"key\":\"true\"}" +
					",{\"name\":\"parentId\",\"type\":\"text\"},{\"name\":\"title\",\"type\":\"text\"}," +
					"{\"name\":\"description\",\"type\":\"text\"},{\"name\":\"cost\",\"type\":\"integer\"}]}]}", result);
	}

	@Test
	public void describeWithNoProvider() {
		Context context = new GaeRestContext();
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setName("testApplication");
		serviceInfo.setVersion("1");
		context.setServiceInfo(serviceInfo);
		String result = converter.describe(context);
		assertEquals(
				"{\"name\":\"testApplication\",\"version\":\"1\"}", result);
	}
	
	@Test
	public void convertMetaDataSetWithReplaceUnique() {
		String result = converter.convert(getComplexEntity(), context);
		assertEquals(
				"{\"name\":\"Example\",\"columns\":" +
					"[{\"name\":\"id\",\"type\":\"integer\",\"key\":\"true\"," +
					"\"unique\":\"true\",\"conflictPolicy\":\"replace\"}," +
					"{\"name\":\"title\",\"type\":\"text\"}],\"children\":[]}",
				result);
	}
	
}
