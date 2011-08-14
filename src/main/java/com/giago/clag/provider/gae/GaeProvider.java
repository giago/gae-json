package com.giago.clag.provider.gae;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;
import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;
import com.giago.clag.model.Options;
import com.giago.clag.provider.AbstractProvider;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author luigi.agosti
 */
public class GaeProvider extends AbstractProvider {
	
	private static final String DOT = ".";

	protected static final GaeQueryHelper QC = new GaeQueryHelper();
	
	protected DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

	@Override
	public Cursor query(String name, String[] projection, String selection,
			String[] selectionArgs, String sortOrder, MetaEntity entity,
			Options options) {
		logger.info("executing query : " + name);
		logger.info("projection : " + projection);
		logger.info("selection : " + selection);
		logger.info("selectionArgs : " + selectionArgs);
		logger.info("sortOrder : " + sortOrder);
		Query q = buildQuery(name, projection, selection, selectionArgs,
				sortOrder);
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		if(entity.getFilterUserIdPropertyName() != null) {
			String userId = null;
			if(user != null) {
				userId = user.getUserId();
			}
			if(entity.getUserIdsPropertyName() != null) {
				q.addFilter(entity.getUserIdsPropertyName(), FilterOperator.EQUAL, userId);
			} else {				
				q.addFilter(entity.getFilterUserIdPropertyName(), FilterOperator.EQUAL, userId);
			}
		}
		if(entity.getEmailPropertyName() != null) {
			String email = null;
			if(user != null) {
				email = user.getEmail();
			}
			q.addFilter(entity.getEmailPropertyName(), FilterOperator.EQUAL, email);
		}
		if(options.getEtag() != -1l) {
			q.addFilter(entity.getEtagPropertyName(), FilterOperator.GREATER_THAN_OR_EQUAL, options.getEtag());
		}
		
		PreparedQuery pq = ds.prepare(q);
		Cursor cursor = new Cursor();
		Collection<String> propertiesMatchingProjection = getPropertyToLookup(
				projection, entity);
		String keyProperty = entity.getKeyProperty();
		for (Entity e : pq.asQueryResultIterable(getFetchOption(options))) {
			for (String property : propertiesMatchingProjection) {
				if (property.equals(keyProperty)) {
					cursor.add(property, e.getKey().getId());
				} else {
					cursor.add(property, e.getProperty(property));
				}
			}
			if(options.isSubObjectFetch()) {
				for(String relation : entity.getRelations()) {
					MetaProperty mp = entity.getMetaProperty(relation);
					addRelations(cursor, mp, options, ds);
				}
			}
			cursor.next();
		}
		return cursor;
	}
	
	@Override
	public Cursor insert(String name, Cursor values, MetaEntity entity) {
		logger.info("Inserting entity " + name + " with values : " + values);
		Cursor result = new Cursor(name);
		String userId = null;
		if(entity.getFilterUserIdPropertyName() != null || entity.getPersistUserIdPropertyName() != null) {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			userId = user.getUserId();
		}
		if(values.getRows().isEmpty()) {
			logger.info("There are not results");
		}
		for (Map<String, Object> row : values.getRows()) {
			logger.info("Checking row");
			Map<String, Object> rowResult = insert(row, entity, userId);
			if(rowResult != null) {
				result.addRow(rowResult);
			}
		}
		return result;
	}
	
	@Override
	public void delete(String name, String remoteId, MetaEntity mds) {
		logger.info("Deleting entity " + name + " with id : " + remoteId);
		long id = (long)Long.valueOf(remoteId);
		ds.delete(KeyFactory.createKey(name, id));
	}
	
	private Map<String, Object> insert(Map<String, Object> row, MetaEntity entity, String userId) {
		logger.info("insert : " + entity.getName());
		if(row.containsKey("_id")) {
			try {
				return update(row, entity);
			} catch (EntityNotFoundException e1) {
				logger.info(e1.getMessage());
			}
		}
		logger.info("not using id");
		Entity e = new Entity(entity.getName());
		for (Entry<String, Object> entry : row.entrySet()) {
			e.setProperty(entry.getKey(), entry.getValue());
		}
		if(userId != null && entity.getPersistUserIdPropertyName() != null) {
			e.setProperty(entity.getPersistUserIdPropertyName(), userId);
		}
		if(userId != null && entity.getFilterUserIdPropertyName() != null) {
			e.setProperty(entity.getFilterUserIdPropertyName(), userId);
		}
		if(userId != null && entity.getUserIdsPropertyName() != null) {
			e.setProperty(entity.getUserIdsPropertyName(), Arrays.asList(userId));
		}
		try {
			double lat = Double.valueOf((String)row.get("lat"));
			double lon = Double.valueOf((String)row.get("lon"));
			Point p = new Point(lat, lon);
	        List<String> cells = GeocellManager.generateGeoCell(p);
			e.setProperty("geocells", cells);
		} catch(Exception aaaah) {
			//TODO
		}
		
		
		Key key = ds.put(e);
		row.put(entity.getKeyProperty(), key.getId());
		return row;					
	}
	
	private Map<String, Object> update(Map<String, Object> row, MetaEntity entity) throws EntityNotFoundException {
		logger.info("using id : " );
		Long id = (Long)row.get("_id");
		Entity toUpdate = ds.get(KeyFactory.createKey(entity.getName(), id));
		for (Entry<String, Object> entry : row.entrySet()) {
			if(entry.getValue() != null && !"_id".equals(entry.getKey())) {
				toUpdate.setProperty(entry.getKey(), entry.getValue());
			}
		}
		ds.put(toUpdate);
		row.put(entity.getKeyProperty(), id);
		return row;
	}

	private void addRelations(Cursor cursor, MetaProperty mp, Options dataLimitation, DatastoreService ds) {
		logger.info("fetching relation for : " + mp.getFrom());
		logger.info("owner : " + mp.getOwner()); //Story
		logger.info("name : " + mp.getName()); //pageId
		Long id = ((Long)cursor.getValueOfCurrentRow("id"));
		Cursor c = query(mp.getOwner(), null, mp.getName() + " = " + id, null, null, dataLimitation);
		c.setName(mp.getOwner());
		cursor.add(mp.getOwner(), c);
	}

	private FetchOptions getFetchOption(Options dl) {
		return FetchOptions.Builder.withLimit(dl.getLimit()).offset(dl.getOffset());
	}

	private Collection<String> getPropertyToLookup(String[] projection,
			MetaEntity entity) {
		Set<String> keys = entity.getPropertyNames();
		if (projection == null) {
			return keys;
		}
		List<String> projectionMatches = new ArrayList<String>();
		for (String projectionProperty : projection) {
			if (keys.contains(projectionProperty)) {
				projectionMatches.add(projectionProperty);
			}
		}
		return projectionMatches;
	}

	private Query buildQuery(String name, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		Query q = new Query(getNameFromFullClassName(name));
		for (GaeQueryHelper.FilterHolder fh : QC.getFilters(selection,
				selectionArgs)) {
			q.addFilter(fh.propertyName, fh.operator, fh.value);
		}
		for (GaeQueryHelper.SortHolder sort : QC.getSorts(sortOrder)) {
			q.addSort(sort.propertyName, sort.direction);
		}
		return q;
	}

	private String getNameFromFullClassName(String name) {
		int index = name.lastIndexOf(DOT);
		if (index < 0) {
			return name;
		}
		return name.substring(index + 1);
	}

}
