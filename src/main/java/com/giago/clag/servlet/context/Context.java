package com.giago.clag.servlet.context;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.giago.clag.converter.Converter;
import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.Options;
import com.giago.clag.provider.Provider;



public interface Context {
	
	interface Parameter {
		String QUERY = "query";
		String SCHEMA = "schema";
		String NAME = "name";
		String PROJECTION = "projection";
		String SELECTION = "selection";
		String SELECTION_ARGS = "selectionArgs";
		String SORT_ORDER = "sortOrder";
		String LIMIT = "limit";
		String OFFSET = "offset";
		String EMAIL = "account";
		String REMOTE_ID = "remoteId";
		String ETAG = "lastUpdate";
	}
	
	String getName();

	boolean isQuery();

	boolean isSchema();
	
	Converter getConverter();
	
	void setConverter(Converter converter);
	
	Provider getProvider();
	
	void setProvider(Provider provider);

	String getSortOrder();

	String[] getSelectionArgs();

	String getSelection();

	String[] getProjection();

	void setRequest(HttpServletRequest req);
	
	Options getFetchOptions();
	
	ServiceInfo getServiceInfo();

	void setServiceInfo(ServiceInfo serviceInfo);

	Cursor getCursorFromRequest(MetaEntity me);

	Cursor getCursorFromJsonDataRequest(MetaEntity me) throws JsonParseException, JsonMappingException, IOException;
	
	String getData();

	String getRemoteId();

}
