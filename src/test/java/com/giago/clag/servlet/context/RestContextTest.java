package com.giago.clag.servlet.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.giago.clag.model.Options;
import com.giago.clag.servlet.context.Context.Parameter;
import com.giago.clag.util.RequestMapBuilder;

public class RestContextTest {

	@Test
	public void shouldGetTheNameFromTheUrlNullWhenNotDefined() {
		GaeRestContext rr = new GaeRestContext();
		rr.setUri("/data/");

		assertNull(rr.getName());
	}

	@Test
	public void shouldGetTheNameFromTheUrl() {
		GaeRestContext rr = new GaeRestContext();
		rr.setUri("/data/Story");

		assertNotNull(rr.getName());
		assertEquals("Story", rr.getName());
	}

	@Test
	public void shouldGetTheNameFromTheUrlWithParameter() {
		GaeRestContext rr = new GaeRestContext();
		rr.setUri("/data/Story?schema");

		assertNotNull(rr.getName());
		assertEquals("Story", rr.getName());
	}

	@Test
	public void shouldGetTheSchemaEvenIfTheParameterIfNull() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add("schema")
				.build());

		assertNotNull(rr.isSchema());
		assertTrue(rr.isSchema());
	}

	@Test
	public void shouldGetTheSchemaEvenIfTheParameterIsTrue() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add("schema",
				"true").build());

		assertNotNull(rr.isSchema());
		assertTrue(rr.isSchema());
	}

	@Test
	public void shouldGetTheSchemaEvenIfTheParameterIsFalse() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add("schema",
				"false").build());

		assertNotNull(rr.isSchema());
		assertFalse(rr.isSchema());
	}

	@Test
	public void shouldGetTheQueryTrueByDefault() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().build());

		assertNotNull(rr.isQuery());
		assertTrue(rr.isQuery());
	}

	@Test
	public void shouldGetTheQueryEvenIfTheParameterIfNull() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add("query")
				.build());

		assertNotNull(rr.isQuery());
		assertTrue(rr.isQuery());
	}

	@Test
	public void shouldGetTheQueryEvenIfTheParameterIsTrue() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add("query",
				"true").build());

		assertNotNull(rr.isQuery());
		assertTrue(rr.isQuery());
	}

	@Ignore
	@Test
	public void shouldGetTheQueryEvenIfTheParameterIsFalse() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add("query",
				"false").build());

		assertNotNull(rr.isQuery());
		assertFalse(rr.isQuery());
	}

	@Ignore
	@Test
	public void shouldGetDefaultFetchOptions() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().build());

		Options o = rr.getFetchOptions();
		assertNotNull(o);
		assertEquals(Options.DEFAULT_LIMIT, o.getLimit());
		assertEquals(Options.DEFAULT_OFFSET, o.getOffset());
	}

	@Ignore
	@Test
	public void shouldGetLimit() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add("limit",
				"10").build());

		Options o = rr.getFetchOptions();
		assertNotNull(o);
		assertEquals(10, o.getLimit());
	}

	@Ignore
	@Test
	public void shouldGetOffset() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add("offset",
				"10").build());

		Options o = rr.getFetchOptions();
		assertNotNull(o);
		assertEquals(10, o.getOffset());
	}

	@Test
	public void shouldGetSelection() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add(
				Parameter.SELECTION, "id").build());
		rr.setUri("/data/Story");

		assertNotNull(rr.getSelection());
		assertEquals("id", rr.getSelection());
	}
	
	@Test
	public void shouldGetSelectionArgs() {
		GaeRestContext rr = new GaeRestContext(new RequestMapBuilder().add(
				Parameter.SELECTION_ARGS, "id").build());
		rr.setUri("/data/Story");

		assertNotNull(rr.getSelectionArgs());
		assertEquals("id", rr.getSelectionArgs()[0]);
	}
	
//	@Test
//	public void shouldGetSelectionArgsFromUri() {
//		RestContext rr = new RestContext(new RequestMapBuilder().build());
//		rr.setUri("/data/Story/1");
//
//		assertNotNull(rr.getSelection());
//		assertEquals("id=1", rr.getSelection());
//	}
	
//	@Ignore
//	@Test
//	public void shouldGetComplexSelectionArgsFromUri() {
//		RestContext rr = new RestContext(new RequestMapBuilder().build());
//		rr.setUri("/data/Page/1/Story/1");
//
//		assertNotNull(rr.getUriSelection());
//		assertEquals("id=1,pageId=1", rr.getSelection());
//	}
	
}
