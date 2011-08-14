package com.giago.clag.servlet.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.giago.clag.converter.Converter;
import com.giago.clag.converter.json.RestProviderConverter;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.provider.Provider;
import com.giago.clag.provider.gae.GaeProvider;
import com.giago.clag.servlet.context.GaeRestContext;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class QueryTest {

	private LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalServiceTestConfig[] {new LocalDatastoreServiceTestConfig()});

	protected DatastoreService ds = DatastoreServiceFactory
			.getDatastoreService();

	@Before
	public void setUp() {
	    helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}
	
	@Test
	public void shouldGetSomeData() {
		Entity e = new Entity(KeyFactory.createKey("Run", Long.valueOf(1)));
		e.setProperty("shared", true);
		e.setProperty("note", "test");
		e.setProperty("date", 123123L);
		e.setProperty("cost", 1200);
		e.setProperty("distance", 12L);
		ds.put(e);
		
		Query action = new Query();
		String result = action.execute(new GaeRestContext(getEmptyParams()) {
			@Override
			public String getName() {
				return "Run";
			}
			@Override
			public Provider getProvider() {
				GaeProvider gp = new GaeProvider();
				MetaEntity me = new MetaEntity("Run", "Run");
				me.addKey("id", Integer.class);
				me.add("note", String.class);
				me.add("date", Date.class);
				me.add("cost", Integer.class);
				me.add("shared", Boolean.class);
				me.add("distance", Long.class);
				gp.add(me);
				return gp;
			}
			@Override
			public Converter getConverter() {
				return new RestProviderConverter();
			}
			
		});
		assertEquals("[{\"shared\":true,\"id\":1,\"distance\":12,\"date\":123123,\"cost\":1200,\"note\":\"test\"}]", result);
	}
	
	@Test
	public void shouldFilterDataWithEtagParameter() {
		Entity e = new Entity(KeyFactory.createKey("Run", Long.valueOf(1)));
		e.setProperty("note", "test1");
		e.setProperty("modified", System.currentTimeMillis() - 1000);
		ds.put(e);
		e = new Entity(KeyFactory.createKey("Run", Long.valueOf(2)));
		e.setProperty("note", "test2");
		e.setProperty("modified", System.currentTimeMillis() + 1000);
		ds.put(e);
		
		Query action = new Query();
		String result = action.execute(new GaeRestContext(getEmptyParams(), System.currentTimeMillis()) {
			@Override
			public String getName() {
				return "Run";
			}
			@Override
			public Provider getProvider() {
				GaeProvider gp = new GaeProvider();
				MetaEntity me = new MetaEntity("Run", "Run");
				me.addKey("id", Integer.class);
				me.add("note", String.class);
				me.add("modified", Long.class);
				me.setEtagPropertyName("modified");
				gp.add(me);
				return gp;
			}
			@Override
			public Converter getConverter() {
				return new RestProviderConverter();
			}
			
		});
		assertTrue(result.startsWith("[{\"id\":2,\"note\":\"test2\",\"modified\":"));
	}
	
	private Map<String, String[]> getEmptyParams() {
		return new HashMap<String, String[]>();
	}

}
