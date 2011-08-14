package com.giago.clag.introspector;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.giago.clag.introspector.jdo.sample.Story;
import com.giago.clag.model.MetaEntity;


@SuppressWarnings("unchecked")
public class AbstractIntrospectorTest {
	
	@Test
	public void shouldGetAllTheClassesOfTheHierarchy() {
		AbstractIntrospector ai = new AbstractIntrospector() {
			@Override
			protected void filterFields(Field field, MetaEntity mds) {
			}

			@Override
			protected MetaEntity analyseClass(Class clazz) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		List<Class> classes = new ArrayList<Class>();
		ai.getClasses(Story.class, classes); 
		assertEquals(4, classes.size());
		
	}

}
