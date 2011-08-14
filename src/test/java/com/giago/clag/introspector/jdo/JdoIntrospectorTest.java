package com.giago.clag.introspector.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.giago.clag.introspector.jdo.sample.ClagAnnotationModel;
import com.giago.clag.introspector.jdo.sample.EtagClagAnnotationModel;
import com.giago.clag.introspector.jdo.sample.PersistUserIdClagAnnotationModel;
import com.giago.clag.introspector.jdo.sample.Story;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;
import com.giago.clag.model.MetaEntity.OnConflictPolicy;

/**
 * @author luigi.agosti
 */
public class JdoIntrospectorTest {
	
	@Test
	public void shouldGetNullResult() {
		MetaEntity jdo = new JdoIntrospector().extractMetaEntity(null);
		assertNull(jdo);
	}
	
	@Test
	public void shouldTheMapHaveTheStringMembersThatAreDeclaredWithAnnotationPersistent(){
		MetaEntity entity = new JdoIntrospector().extractMetaEntity(Story.class);
		assertNotNull(entity);
		
		assertTrue(entity.contains("mediaHref"));
		assertEquals(MetaEntity.Type.STRING.getValue(), entity.getMetaProperty("mediaHref").getType());
		assertTrue(entity.contains("title"));
		assertEquals(MetaEntity.Type.STRING.getValue(), entity.getMetaProperty("title").getType());
		assertTrue(entity.contains("mediaImageHref"));
		assertEquals(MetaEntity.Type.STRING.getValue(), entity.getMetaProperty("mediaImageHref").getType());
		assertTrue(entity.contains("caption"));
		assertEquals(MetaEntity.Type.STRING.getValue(), entity.getMetaProperty("caption").getType());
		assertTrue(entity.contains("copy"));
		assertEquals(MetaEntity.Type.STRING.getValue(), entity.getMetaProperty("copy").getType());
	}
	
	@Test
	public void shouldHidePropertiesMarkedWithIsHidden(){
		MetaEntity entity = new JdoIntrospector().extractMetaEntity(Story.class);
		assertNotNull(entity);
		
		assertFalse(entity.contains("groupId"));
	}

	@Test
	public void shouldMetaEntityGiveBackCorrectNameClass(){
		MetaEntity entity = new JdoIntrospector().extractMetaEntity(Story.class);
		assertNotNull(entity);
		assertNotNull(entity.getClassName());
		assertEquals(Story.class.getName(), entity.getClassName());
	}

	@Test
	public void shouldMetaEntityGiveBackCorrectName(){
		MetaEntity entity = new JdoIntrospector().extractMetaEntity(Story.class);
		assertNotNull(entity);
		assertNotNull(entity.getName());
		assertEquals(Story.class.getSimpleName(), entity.getName());
	}

	@Test
	public void shouldTheMapHaveTheKey(){
		MetaEntity entity = new JdoIntrospector().extractMetaEntity(Story.class);
		assertNotNull(entity);
		
		assertTrue(entity.contains("mediaHref"));
		MetaProperty mp = entity.getMetaProperty("mediaHref");
		assertEquals(MetaEntity.Type.STRING.getValue(), mp.getType());
		assertFalse(mp.getKey());
		
		assertTrue(entity.contains("id"));
		mp = entity.getMetaProperty("id");
		assertEquals(MetaEntity.Type.INTEGER.getValue(), mp.getType());
		assertTrue(mp.getKey());
	}
	
	@Test
	public void shouldUseClagProperly() {
		MetaEntity entity = new JdoIntrospector().extractMetaEntity(ClagAnnotationModel.class);
		assertNotNull(entity);
		
		assertTrue(entity.contains("id"));
		MetaProperty mp = entity.getMetaProperty("id");
		assertEquals(MetaEntity.Type.INTEGER.getValue(), mp.getType());
		assertEquals(OnConflictPolicy.REPLACE, mp.getOnConflictPolicy());
		assertTrue(mp.getKey());
		assertTrue(mp.getUnique());
		
		assertTrue(entity.contains("field"));
		mp = entity.getMetaProperty("field");
		assertEquals(MetaEntity.Type.STRING.getValue(), mp.getType());
		assertEquals(OnConflictPolicy.NOT_DEFINED, mp.getOnConflictPolicy());
		assertFalse(mp.getKey());
		assertFalse(mp.getUnique());
		
		assertEquals("userId", entity.getFilterUserIdPropertyName());
	}
	
	@Test
	public void shouldUseClagProperlyWithPersistUserId() {
		MetaEntity entity = new JdoIntrospector().extractMetaEntity(PersistUserIdClagAnnotationModel.class);
		assertNotNull(entity);
		
		assertTrue(entity.contains("id"));
		MetaProperty mp = entity.getMetaProperty("id");
		assertEquals(MetaEntity.Type.INTEGER.getValue(), mp.getType());
		assertEquals(OnConflictPolicy.REPLACE, mp.getOnConflictPolicy());
		assertTrue(mp.getKey());
		assertTrue(mp.getUnique());
		
		assertTrue(entity.contains("field"));
		mp = entity.getMetaProperty("field");
		assertEquals(MetaEntity.Type.STRING.getValue(), mp.getType());
		assertEquals(OnConflictPolicy.NOT_DEFINED, mp.getOnConflictPolicy());
		assertFalse(mp.getKey());
		assertFalse(mp.getUnique());
		
		assertEquals("userId", entity.getPersistUserIdPropertyName());
	}
	
	@Test
	public void shouldConvertBooleanPropertyToInteger() {
		MetaEntity entity = new JdoIntrospector().extractMetaEntity(ClagAnnotationModel.class);
		assertNotNull(entity);
		
		assertTrue(entity.contains("enabled"));
		MetaProperty mp = entity.getMetaProperty("enabled");
		assertEquals(MetaEntity.Type.BOOLEAN.getValue(), mp.getType());
	}
	
	@Test
	public void shouldSeeEtagAnnotation() {
		MetaEntity entity = new JdoIntrospector().extractMetaEntity(EtagClagAnnotationModel.class);
		assertNotNull(entity);
		
		assertTrue(entity.contains("modified"));
		MetaProperty mp = entity.getMetaProperty("modified");
		assertTrue(mp.isEtag());
	}
	
	
}
