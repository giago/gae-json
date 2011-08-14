package com.giago.clag.provider;

import java.util.Collection;

import com.giago.clag.introspector.Introspector;
import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.Options;
import com.giago.clag.util.Configurable;


/**
 * @author luigi.agosti
 */
public interface Provider extends Configurable {

	void setIntrospector(Introspector introspector);

	Cursor query(String name, String[] projection, String selection,
			String[] selectionArgs, String sortOrder, MetaEntity entity);

	Cursor query(String name, String[] projection, String selection,
			String[] selectionArgs, String sortOrder, MetaEntity entity,
			Options dataLimitation);

	Cursor query(String name, String[] projection, String selection,
			String[] selectionArgs, String sortOrder);

	Cursor query(String name, String[] projection, String selection,
			String[] selectionArgs, String sortOrder,
			Options dataLimitation);
	
	Cursor insert(String name, Cursor values, MetaEntity entity);

	MetaEntity schema(String name);
	
	Collection<MetaEntity> schema();

	void add(Class<?> clazz);

	void add(MetaEntity entity);

	void linkMetaEntities();

	void delete(String name, String remoteId, MetaEntity mds);
	
}
