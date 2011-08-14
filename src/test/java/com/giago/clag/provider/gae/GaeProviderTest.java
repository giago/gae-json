package com.giago.clag.provider.gae;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.giago.clag.introspector.jdo.JdoIntrospector;
import com.giago.clag.introspector.jdo.sample.ClagAnnotationModel;
import com.giago.clag.introspector.jdo.sample.Page;
import com.giago.clag.introspector.jdo.sample.PersistUserIdClagAnnotationModel;
import com.giago.clag.introspector.jdo.sample.Story;
import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;
import com.giago.clag.model.Options;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

public class GaeProviderTest {

	private LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalServiceTestConfig[] {new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig()});

	protected DatastoreService ds = DatastoreServiceFactory
			.getDatastoreService();

	@Before
	public void setUp() {
		HashMap<String, Object> envAttr = new HashMap<String, Object>();
	    envAttr.put("com.google.appengine.api.users.UserService.user_id_key", "10");
	    helper.setEnvEmail("test@example.com").setEnvAuthDomain("example.com")
	    	.setEnvAttributes(envAttr).setEnvIsLoggedIn(true);
	    helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	private GaeProvider provider = new GaeProvider();

	@Test
	public void shouldGetMetaInformationAboutId() {
		provider.setIntrospector(new JdoIntrospector());
		provider.add(Story.class);

		MetaEntity entity = provider.schema(Story.class.getSimpleName());
		assertNotNull(entity);

		MetaProperty p = entity.getMetaProperty("id");
		assertNotNull(p);
	}

	@Test
	public void shouldGetIdValues() {
		Entity e = new Entity(Story.class.getSimpleName());
		Key key = ds.put(e);
		long longId = key.getId();
		
		provider.setIntrospector(new JdoIntrospector());
		provider.add(Story.class);

		Cursor cursor = provider.query(Story.class.getSimpleName(), null, null,
				null, null);
		
		assertNotNull(cursor);
		assertNotNull(cursor.getRows());
		assertEquals(1, cursor.getRows().size());
		assertEquals(longId, cursor.getRows().get(0).get("id"));
	}
	
	@Test
	public void shouldGetLimitedResult() {
		Entity e = new Entity(Story.class.getSimpleName());
		ds.put(e);
		e = new Entity(Story.class.getSimpleName());
		ds.put(e);
		
		provider.setIntrospector(new JdoIntrospector());
		provider.add(Story.class);

		Options options = new Options();
		options.setLimit(1);
		
		Cursor cursor = provider.query(Story.class.getSimpleName(), null, null,
				null, null, options);
		
		assertNotNull(cursor);
		assertNotNull(cursor.getRows());
		assertEquals(1, cursor.getRows().size());
	}
	
	@Test
	public void shouldGetResultWithCorrectOffset() {
		Entity e = new Entity(Story.class.getSimpleName());
		ds.put(e);
		e = new Entity(Story.class.getSimpleName());
		Key key2 = ds.put(e);
		
		provider.setIntrospector(new JdoIntrospector());
		provider.add(Story.class);

		Options options = new Options();
		options.setLimit(1);
		options.setOffset(1);
		
		Cursor cursor = provider.query(Story.class.getSimpleName(), null, null,
				null, null, options);
		
		assertNotNull(cursor);
		assertNotNull(cursor.getRows());
		assertEquals(1, cursor.getRows().size());
		assertEquals(key2.getId(), cursor.getRows().get(0).get("id"));
	}
	
	@Test
	public void shouldGetFilterByUserId() {
		Entity e = new Entity(ClagAnnotationModel.class.getSimpleName());
		e.setProperty("userId", "10");
		e.setProperty("field", "ok");
		ds.put(e);
		e = new Entity(ClagAnnotationModel.class.getSimpleName());
		e.setProperty("userId", "13");
		e.setProperty("field", "umm wrong");
		ds.put(e);
		
		provider.setIntrospector(new JdoIntrospector());
		provider.add(ClagAnnotationModel.class);
		
		Cursor cursor = provider.query(ClagAnnotationModel.class.getSimpleName(), null, null,
				null, null, new Options());
		
		assertNotNull(cursor);
		assertNotNull(cursor.getRows());
		assertEquals(1, cursor.getRows().size());
		Map<String, Object> cv = cursor.getRows().get(0);
		assertTrue(cv.containsKey("field"));
		assertEquals("ok", cv.get("field"));
		assertFalse(cv.containsKey("userId"));
	}
	
	@Test
	public void shouldGetSkipFilterByUserId() {
		Entity e = new Entity(PersistUserIdClagAnnotationModel.class.getSimpleName());
		e.setProperty("userId", "10");
		e.setProperty("field", "ok");
		ds.put(e);
		e = new Entity(PersistUserIdClagAnnotationModel.class.getSimpleName());
		e.setProperty("userId", "13");
		e.setProperty("field", "umm wrong");
		ds.put(e);
		
		provider.setIntrospector(new JdoIntrospector());
		provider.add(PersistUserIdClagAnnotationModel.class);
		
		Cursor cursor = provider.query(PersistUserIdClagAnnotationModel.class.getSimpleName(), null, null,
				null, null, new Options());
		
		assertNotNull(cursor);
		assertNotNull(cursor.getRows());
		assertEquals(2, cursor.getRows().size());
	}
	
	@Test
	public void shouldGetFilterByUserIdIfTheUserIsNotLoggedIn() {
		HashMap<String, Object> envAttr = new HashMap<String, Object>();
	    envAttr.put("com.google.appengine.api.users.UserService.user_id_key", "10");
	    helper.setEnvEmail("test@example.com").setEnvAuthDomain("example.com")
	    	.setEnvAttributes(envAttr).setEnvIsLoggedIn(false);
	    helper.setUp();
	    
		Entity e = new Entity(ClagAnnotationModel.class.getSimpleName());
		e.setProperty("userId", "10");
		e.setProperty("field", "ok");
		ds.put(e);
		e = new Entity(ClagAnnotationModel.class.getSimpleName());
		e.setProperty("userId", "13");
		e.setProperty("field", "umm wrong");
		ds.put(e);
		
		provider.setIntrospector(new JdoIntrospector());
		provider.add(ClagAnnotationModel.class);
		
		Cursor cursor = provider.query(ClagAnnotationModel.class.getSimpleName(), null, null,
				null, null, new Options());
		
		assertNotNull(cursor);
		assertNotNull(cursor.getRows());
		assertEquals(0, cursor.getRows().size());
	}
	
	@Test
	public void shouldGetListProperty() {
		Entity e = new Entity(Page.class.getSimpleName());
		e.setProperty("groupIds", Arrays.asList("1", "2"));
		ds.put(e);
		
		provider.setIntrospector(new JdoIntrospector());
		provider.add(Page.class);

		Options options = new Options();
		options.setLimit(1);
		
		Cursor cursor = provider.query(Page.class.getSimpleName(), null, null,
				null, null, options);
		
		assertNotNull(cursor);
		assertNotNull(cursor.getRows());
		assertEquals(1, cursor.getRows().size());
		
		Map<String, Object> properties = cursor.getRows().get(0);
		assertNotNull(properties);
		assertTrue(properties.containsKey("groupIds"));
		assertEquals(Arrays.asList("1", "2"), properties.get("groupIds"));
	}
	
	@Test
	public void shouldInsertACursor() {
		MetaEntity me = new MetaEntity("Example", "Example");
		me.addKey("id", Integer.class);
		me.add("textProperty", String.class);
		me.add("integerProperty", Integer.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("textProperty", "test");
		
		Cursor cursor = new Cursor(me.getName());
		cursor.addRow(map);
			
		Cursor result = provider.insert(me.getName(), cursor, me);
		
		assertNotNull(result);
		assertEquals(1, result.getRows().size());
		Map<String, Object> row = result.getRows().get(0);  
		assertTrue(row.containsKey("textProperty"));
		assertEquals("test", row.get("textProperty"));
		assertTrue(row.containsKey("id"));
		assertEquals(Long.valueOf(1), row.get("id"));
	}
	
//	@Test
//	public void shouldInsertACursor() {
//		MetaEntity me = new MetaEntity("Example", "Example");
//		me.addKey("id", Integer.class);
//		me.add("textProperty", String.class);
//		me.add("integerProperty", Integer.class);
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("textProperty", "test");
//		
//		Cursor cursor = new Cursor(me.getName());
//		cursor.addRow(map);
//			
//		Cursor result = provider.insert(me.getName(), cursor, me);
//		
//		assertNotNull(result);
//		assertEquals(1, result.getRows().size());
//		Map<String, Object> row = result.getRows().get(0);  
//		assertTrue(row.containsKey("textProperty"));
//		assertEquals("test", row.get("textProperty"));
//		assertTrue(row.containsKey("id"));
//		assertEquals(Long.valueOf(1), row.get("id"));
//	}

}
