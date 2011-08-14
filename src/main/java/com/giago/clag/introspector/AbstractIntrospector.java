package com.giago.clag.introspector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.giago.clag.introspector.annotation.Clag;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;


/**
 * @author luigi.agosti
 */
@SuppressWarnings("unchecked")
public abstract class AbstractIntrospector implements Introspector {

	@Override
	public MetaEntity extractMetaEntity(Class classToParse) {
		if(classToParse == null) {
			return null;
		}
		MetaEntity me = analyseClass(classToParse);
		List<Class> classes = new ArrayList<Class>();
		getClasses(classToParse, classes);
		for(Field field : getFields(classes)) {
			Clag c = (Clag)field.getAnnotation(Clag.class);
			if(c == null || !c.hidden()) {
				filterFields(field, me);
			} else if(c != null && c.hidden() && c.filterUserId()) {
				me.setFilterUserIdPropertyName(field.getName());
			} else if(c != null && c.hidden() && c.persistUserId()) {
				me.setPersistUserIdPropertyName(field.getName());
			} else if(c != null && c.hidden() && c.email()) {
				me.setEmailPropertyName(field.getName());
			} else if(c != null && c.hidden() && c.etag()) {
				me.setEtagPropertyName(field.getName());
			}
		}
		return me;
	}
	
	@Override
	public void linking(Map<String, MetaEntity> metaEntities) {
		Map<String, List<MetaProperty>> relations = new HashMap<String, List<MetaProperty>>();
		for(String key : metaEntities.keySet()) {
			MetaEntity me = metaEntities.get(key);
			for(String rKey : me.getRelations()) {
				MetaProperty mp = me.getMetaProperty(rKey);
				if(relations.containsKey(mp.getFrom())) {
					relations.get(mp.getFrom()).add(mp);
				} else {
					List<MetaProperty> rs = new ArrayList<MetaProperty>();
					rs.add(mp);
					relations.put(mp.getFrom(), rs);
				}
			}			
		}
		
		for(String key : metaEntities.keySet()) {
			MetaEntity me = metaEntities.get(key);
			me.resetRelations();
		}
		
		for(String key : relations.keySet()) {
			if(metaEntities.containsKey(key)) {
				MetaEntity me = metaEntities.get(key);
				for(MetaProperty mp : relations.get(key)) {
					me.addRelation(mp.getName(), mp.getOwner(), mp.getFrom(), mp.getClazz(), mp.isInclude());
				}
			}
		}
	}
	
	protected abstract void filterFields(Field field, MetaEntity mds);
	
	protected abstract MetaEntity analyseClass(Class clazz);
	
	protected void getClasses(Class clazz, List<Class> classes) {
		Class superClass = clazz.getSuperclass();
		if(superClass != null) {
			getClasses(superClass, classes);
		}
		classes.add(clazz);
	}
	
	protected List<Field> getFields(List<Class> classes) {
		List<Field> allFields = new ArrayList<Field>();
		for(Class clazz : classes) {
			allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}
		return allFields;
	}
	
}
