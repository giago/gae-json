package com.giago.clag.converter.json;

import java.util.Date;
import java.util.Map;


import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;
import com.giago.clag.servlet.context.Context;
import com.giago.clag.servlet.context.ServiceInfo;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONStringer;

/**
 * @author luigi.agosti
 */
public class RestProviderConverter extends BaseConverter {

	private static final String TABLE = "name";

	private static final String COLUMNS = "columns";

	private static final String NAME = "name";

	private static final String TYPE = "type";

	private static final String KEY = "key";
	
	private static final String UNIQUE = "unique";
	
	private static final String CONFLICT_POLICY = "conflictPolicy";

	private static final String KEY_VALUE = "true";

	private static final String SCHEMA = "schema";
	
	private static final String SERVICES = "services";
	
	private static final String VERSION = "version";
	
	private static final String CHILDREN = "children";

	private static final String GET = "get";

	@Override
	public String convert(MetaEntity entity, Context context) {
		try {
			JSONStringer jsonStringer = new JSONStringer();
			converEntity(jsonStringer, entity, context, true);
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
					jsonStringer.key(SERVICES).array().object();				
					jsonStringer.key(GET).array();
					for(MetaEntity entity: context.getProvider().schema()) {
						jsonStringer.value(entity.getName());
					}
					jsonStringer.endArray();
					
					jsonStringer.endObject().endArray();
				
					jsonStringer.key(SCHEMA).array();
					for(MetaEntity entity: context.getProvider().schema()) {
						converEntity(jsonStringer, entity, context, true);
					}
					jsonStringer.endArray();
				}
			}
			return jsonStringer.endObject().toString();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void converEntity(JSONStringer jsonStringer, MetaEntity entity, Context context, boolean recursive)
			throws Exception {
		jsonStringer.object().key(TABLE).value(entity.getName()).key(COLUMNS)
				.array();
		for (MetaProperty md : entity.getMetaProperties()) {
			jsonStringer.object().key(NAME).value(md.getName()).key(TYPE)
					.value(md.getType());
			if (md.getKey()) {
				jsonStringer.key(KEY).value(KEY_VALUE);
			}
			if (md.getUnique()) {
				jsonStringer.key(UNIQUE).value(KEY_VALUE);
			}
			if (md.hasConflictPolicy()) {
				jsonStringer.key(CONFLICT_POLICY).value(md.getOnConflictPolicy().name().toLowerCase());
			}
			jsonStringer.endObject();
		}
		jsonStringer.endArray();
		
		if(recursive) {
			jsonStringer.key(CHILDREN).array();
			for(String name: entity.getRelations()) {
				MetaProperty mp = entity.getMetaProperty(name);
				if(entity.getName().equals(mp.getOwner())) {
					recursive = false;
				}
				converEntity(jsonStringer, context.getProvider().schema(mp.getOwner()), context, recursive);
			}
			jsonStringer.endArray();
		}
		jsonStringer.endObject();
	}

	@Override
	public String convertIdsOnly(Cursor cursor, MetaEntity mds, Context context) {
		return convert(cursor, mds, context);
	}

}