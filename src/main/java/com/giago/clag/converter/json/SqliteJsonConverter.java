package com.giago.clag.converter.json;

import java.util.Date;
import java.util.Map;


import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;
import com.giago.clag.model.MetaEntity.OnConflictPolicy;
import com.giago.clag.servlet.context.Context;
import com.giago.clag.servlet.context.ServiceInfo;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONStringer;

/**
 * @author luigi.agosti
 */
public class SqliteJsonConverter extends BaseConverter {

	private static final String NAME = "name";
	
	private static final String VERSION = "version";
	
	private static final String DROP_STATEMENTS = "dropStatements";

	private static final String CREATE_STATEMENTS = "createStatements";

	@Override
	public String convert(MetaEntity entity, Context context) {
		try {
			JSONStringer jsonStringer = new JSONStringer();
			jsonStringer.object().key(DROP_STATEMENTS);
			dropStm(jsonStringer, entity, context, false);
			jsonStringer.key(CREATE_STATEMENTS);
			createStm(jsonStringer, entity, context, false);
			jsonStringer.endObject();
			return jsonStringer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String convert(Cursor cursor, MetaEntity entity, Context context) {
		try {
			JSONStringer jsonStringer = new JSONStringer();
			jsonStringer.array();
			convert(jsonStringer, cursor, context);
			return jsonStringer.endArray().toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void convert(JSONStringer jsonStringer, Cursor cursor, Context context) throws JSONException {
		for (Map<String, Object> row : cursor.getRows()) {
			jsonStringer.object();
			for (String key : row.keySet()) {
				Object obj = row.get(key);
				if (obj instanceof Date) {
					jsonStringer.key(key).value(((Date) obj).getTime());
				} else if(obj instanceof Cursor) {
					Cursor subCursor = (Cursor)obj;
					jsonStringer.key(subCursor.getName()).array();
					convert(jsonStringer, subCursor, context);
					
					jsonStringer.endArray();
				} else {
					jsonStringer.key(key).value(row.get(key));
				}
			}
			jsonStringer.endObject();
		}
	}
	
	@Override
	public String describe(Context context) {
		try {
			JSONStringer jsonStringer = new JSONStringer();
			jsonStringer.object();
			if(context != null) {
				ServiceInfo si = context.getServiceInfo();
				if(si != null) {
					jsonStringer.key(NAME).value(si.getName()).
						key(VERSION).value(si.getVersion());
				}
				if(context.getProvider() != null) {
					dropPart(jsonStringer, context);
					createPart(jsonStringer, context);
				}
			}
			return jsonStringer.endObject().toString();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void dropPart(JSONStringer jsonStringer, Context context) throws Exception {
		jsonStringer.key(DROP_STATEMENTS).array();
		for(MetaEntity entity: context.getProvider().schema()) {
			dropStm(jsonStringer, entity, context, true);
		}
		jsonStringer.endArray();
	}
	
	private void createPart(JSONStringer jsonStringer, Context context) throws Exception {
		jsonStringer.key(CREATE_STATEMENTS).array();
		for(MetaEntity entity: context.getProvider().schema()) {
			createStm(jsonStringer, entity, context, true);
		}
		jsonStringer.endArray();
	}
	
	private void createStm(JSONStringer jsonStringer, MetaEntity entity, Context context, boolean withTriggers)
			throws Exception {
		StringBuilder createStatement = new StringBuilder("create table if not exists " + entity.getName() 
				+ "(_id integer primary key autoincrement,");
		for(MetaProperty prop : entity.getMetaProperties()) {
			createStatement.append(prop.getName()).append(" ").append(prop.getType());
			if(prop.getUnique()) {
				createStatement.append(" unique");
				if(prop.getOnConflictPolicy() == OnConflictPolicy.REPLACE) {
					createStatement.append(" on conflict replace");
				}
			}
			createStatement.append(",");
		}
		createStatement.deleteCharAt(createStatement.lastIndexOf(","));
		createStatement.append(");");
		jsonStringer.value(createStatement);
	}

	private void dropStm(JSONStringer jsonStringer, MetaEntity entity, Context context, boolean withTriggers)
		throws Exception {
		jsonStringer.value(new StringBuilder("drop table if exists " + entity.getName() + ";"));
	}

	@Override
	public String convertIdsOnly(Cursor cursor, MetaEntity mds, Context context) {
		try {
			JSONStringer jsonStringer = new JSONStringer();
			jsonStringer.array();
			convertIdsOnly(jsonStringer, cursor, context);
			return jsonStringer.endArray().toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//TODO need to make it right!!! not a copy of the other one
	private void convertIdsOnly(JSONStringer jsonStringer, Cursor cursor, Context context) throws JSONException {
		for (Map<String, Object> row : cursor.getRows()) {
			jsonStringer.object();
			for (String key : new String[]{"id", "_id"}) {
				jsonStringer.key(key).value((Long)row.get(key));
			}
			jsonStringer.endObject();
		}
	}

}
