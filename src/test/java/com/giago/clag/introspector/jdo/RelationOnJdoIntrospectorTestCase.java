package com.giago.clag.introspector.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.Test;

import com.giago.clag.introspector.jdo.sample.Page;
import com.giago.clag.introspector.jdo.sample.Story;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;

/**
 * @author luigi.agosti
 */
public class RelationOnJdoIntrospectorTestCase {
	
	private JdoIntrospector introspector = new JdoIntrospector();
	
	@Test
	public void shouldGetKeyInTheDeclaringEntity() {
		MetaEntity me = introspector.extractMetaEntity(Story.class);
		List<String> relations = me.getRelations();
		assertNotNull(relations);
		assertEquals(1, relations.size());
		assertTrue(relations.contains("pageId"));
		MetaProperty mp = me.getMetaProperty("pageId");
		assertNotNull(mp);
		assertEquals("Page", mp.getFrom());
		assertTrue(mp.isInclude());
	}
	
	@Test
	public void shouldGetKeyInTheDeclaringEntitySelfReference() {
		MetaEntity me = introspector.extractMetaEntity(Page.class);
		List<String> relations = me.getRelations();
		assertNotNull(relations);
		assertEquals(1, relations.size());
		assertTrue(relations.contains("parentKeyId"));
		MetaProperty mp = me.getMetaProperty("parentKeyId");
		assertNotNull(mp);
		assertEquals("Page", mp.getFrom());
		assertFalse(mp.isInclude());
	}
	
	@Test
	public void shouldMoveRelations() {
		Map<String, MetaEntity> map = new HashMap<String, MetaEntity>();
		MetaEntity mePage = introspector.extractMetaEntity(Page.class);
		map.put(Page.class.getSimpleName(), mePage);
		MetaEntity meStory = introspector.extractMetaEntity(Story.class);
		map.put(Story.class.getSimpleName(), meStory);
		
		introspector.linking(map);
		
		assertNotNull(map);
		
		assertEquals(2, map.values().size());
		MetaEntity page = map.get(Page.class.getSimpleName());
		assertNotNull(page);
		assertEquals("Page", page.getName());
		List<String> relations =  page.getRelations();
		assertNotNull(relations);
		assertEquals(2, relations.size());
		assertEquals("pageId", relations.get(0));
		assertEquals("Page", map.get("Page").getMetaProperty(relations.get(0)).getFrom());
		assertEquals("Story", map.get("Page").getMetaProperty(relations.get(0)).getOwner());
		assertEquals("parentKeyId", relations.get(1));
		assertEquals("Page", map.get("Page").getMetaProperty(relations.get(1)).getFrom());
		assertEquals("Page", map.get("Page").getMetaProperty(relations.get(1)).getOwner());
		
		MetaEntity story = map.get(Story.class.getSimpleName());
		assertNotNull(story);
		assertEquals("Story", story.getName());
		relations =  story.getRelations();
		assertNotNull(relations);
		assertEquals(0, relations.size());
		
	}
	
	@Test
	public void shouldRemoveRelationsIfTargetsAreNotInTheWatchingList() {
		Map<String, MetaEntity> map = new HashMap<String, MetaEntity>();
		MetaEntity meStory = introspector.extractMetaEntity(Story.class);
		map.put(Story.class.getSimpleName(), meStory);
		
		MetaEntity story = map.get(Story.class.getSimpleName());
		
		introspector.linking(map);
		
		assertNotNull(map);
		
		assertNotNull(story);
		assertEquals("Story", story.getName());
		List<String> relations =  story.getRelations();
		assertNotNull(relations);
		assertEquals(0, relations.size());
	}
	
	
}
