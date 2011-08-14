package com.giago.clag.servlet.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DeleteTest {

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
	public void shouldDelete() {
		Key key = KeyFactory.createKey("Run", 1);
		Entity e = new Entity(key);
		e.setProperty("_id", Long.valueOf(12));
		e.setProperty("id", 1);
		key = ds.put(e);
		final long id = key.getId();
		
		assertTrue(entityExists(key));
		
		Delete deleteAction = new Delete();
		String result = deleteAction.execute(new GaeRestContext() {
			@Override
			public String getName() {
				return "Run";
			}
			@Override
			public Provider getProvider() {
				GaeProvider gp = new GaeProvider();
				MetaEntity me = new MetaEntity("Run", "Run");
				me.addKey("id", Long.class);
				me.add("_id", Long.class);
				gp.add(me);
				return gp;
			}
			@Override
			public Converter getConverter() {
				return new RestProviderConverter();
			}
			@Override
			public String getRemoteId() {
				return "" + id;
			}
		});
		
		assertFalse(entityExists(key));
		
		assertEquals("{\"id\":1}", result);
	}
	
	private boolean entityExists(Key key) {
		try {
			ds.get(key);
			return true;
		} catch (EntityNotFoundException e1) {
			return false;
		}
	}
	
}
