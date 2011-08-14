package com.giago.clag.converter.json;

import com.giago.clag.converter.Converter;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;
import com.giago.clag.model.MetaEntity.OnConflictPolicy;
import com.giago.clag.provider.Provider;
import com.giago.clag.provider.gae.GaeProvider;
import com.giago.clag.servlet.context.Context;
import com.giago.clag.servlet.context.GaeRestContext;


public abstract class AbstractConverterTest {

	protected Context context = new GaeRestContext();
	
	protected Converter converter = initConverter();

	protected Provider getSampleProvider() {
		Provider provider = new GaeProvider();
		provider.add(getSampleEntity());
		return provider;
	}
	
	protected MetaEntity getSampleEntity() {
		MetaEntity entity = new MetaEntity("novoda.clag.Example", "Example");
		entity.add(new MetaProperty.Builder("title").clazz(String.class).build());
		entity.add(new MetaProperty.Builder("description").clazz(String.class).build());
		entity.add(new MetaProperty.Builder("cost").clazz(Integer.class).build());
		entity.add(new MetaProperty.Builder("id").clazz(Integer.class).key(true).build());
		return entity;
	}

	protected MetaEntity getComplexEntity() {
		MetaEntity entity = new MetaEntity("novoda.clag.Example", "Example");
		entity.add(new MetaProperty.Builder("title").clazz(String.class).build());
		entity.add(new MetaProperty.Builder("id").clazz(Integer.class)
				.unique(true).onConflictPolicy(OnConflictPolicy.REPLACE).key(
						true).build());
		return entity;
	}
	
	protected abstract Converter initConverter();
}
