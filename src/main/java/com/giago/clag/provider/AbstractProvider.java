package com.giago.clag.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.giago.clag.introspector.Introspector;
import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.Options;


/**
 * @author luigi.agosti
 */
public abstract class AbstractProvider implements Provider {

	protected static final Logger logger = Logger
			.getLogger(AbstractProvider.class.getName());

	protected Map<String, MetaEntity> entities = new HashMap<String, MetaEntity>();

	protected Introspector introspector;

	@Override
	public void isConfigured() {
		logger.info("Checking configuration");
		if (introspector == null) {
			throw new RuntimeException(
					"Introspector has not been set in the content provider");
		} else {
			logger.info("Introspector is set.");
		}
		if (entities.isEmpty()) {
			throw new RuntimeException(
					"No Entity has been set in the content provider");
		} else {
			for (String entityKey : entities.keySet()) {
				logger.info("Entities are : " + entityKey);
			}
		}
	}

	@Override
	public void setIntrospector(Introspector introspector) {
		this.introspector = introspector;
	}

	@Override
	public void add(Class<?> clazz) {
		MetaEntity entity = introspector.extractMetaEntity(clazz);
		if (entity != null) {
			logger.info("Adding Entity : " + clazz.getSimpleName() + ","
					+ clazz);
			entities.put(clazz.getSimpleName(), entity);
		} else {
			throw new RuntimeException(
					"Faild to get entity description out of the class "
							+ clazz);
		}
	}
	
	@Override
	public void linkMetaEntities() {
		introspector.linking(entities);
	}
	
	@Override
	public void add(MetaEntity entity) {
		if (entity != null && entity.getName() != null) {
			entities.put(entity.getName(), entity);
		} else {
			throw new RuntimeException(
					"Faild to get entity information : name or entityt are null");
		}
	}

	@Override
	public MetaEntity schema(String name) {
		if (!entities.containsKey(name)) {
			logger.info("Schema for entity name : " + name
					+ " CANNOT be found!");
			return null;
		}
		return entities.get(name);
	}
	
	@Override
	public Collection<MetaEntity> schema() {
		return entities.values();
	}

	@Override
	public Cursor query(String name, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return query(name, projection, selection, selectionArgs, sortOrder,
				schema(name));
	}

	@Override
	public Cursor query(String name, String[] projection, String selection,
			String[] selectionArgs, String sortOrder, MetaEntity entity) {
		return query(name, projection, selection, selectionArgs, sortOrder,
				entity, Options.getDefault());
	}

	@Override
	public Cursor query(String name, String[] projection, String selection,
			String[] selectionArgs, String sortOrder,
			Options dataLimitation) {
		return query(name, projection, selection, selectionArgs, sortOrder,
				schema(name), dataLimitation);
	}

}
