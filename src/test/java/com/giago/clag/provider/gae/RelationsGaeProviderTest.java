package com.giago.clag.provider.gae;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.giago.clag.introspector.jdo.JdoIntrospector;
import com.giago.clag.introspector.jdo.sample.Page;
import com.giago.clag.introspector.jdo.sample.Story;
import com.giago.clag.model.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class RelationsGaeProviderTest {

	private LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	protected DatastoreService ds = DatastoreServiceFactory
			.getDatastoreService();

	private GaeProvider provider;

	@Before
	public void setUp() {
		helper.setUp();
		provider = new GaeProvider();
		provider.setIntrospector(new JdoIntrospector());
		provider.add(Page.class);
		provider.add(Story.class);
		provider.linkMetaEntities();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}
	
	@Test
	public void shouldGetPageWithStories() {
		Entity e = new Entity(Page.class.getSimpleName());
		e.setProperty("title", "page title");
		Key key = ds.put(e);
		long pageId = key.getId();
		e = new Entity(Story.class.getSimpleName());
		e.setProperty("pageId", pageId);
		key = ds.put(e);
		long storyId = key.getId();
		

		Cursor cursor = provider.query(Page.class.getSimpleName(), null, null,
				null, null);
		
		assertNotNull(cursor);
		assertNotNull(cursor.getRows());
		assertEquals(1, cursor.getRows().size());
		Map<String, Object> row = cursor.getRows().get(0);
		assertEquals(pageId, row.get("id"));
		Cursor storyCursor = (Cursor)row.get(Story.class.getSimpleName());
		assertNotNull(storyCursor);
		assertNotNull(storyCursor.getName());
		assertEquals("Story", storyCursor.getName());
		assertNotNull(storyCursor.getRows());
		assertEquals(1, storyCursor.getRows().size());
		Map<String, Object> storyRow = storyCursor.getRows().get(0);
		assertEquals(storyId, storyRow.get("id"));
	}
	
}
