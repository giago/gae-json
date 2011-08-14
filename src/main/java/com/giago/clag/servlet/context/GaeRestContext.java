package com.giago.clag.servlet.context;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.giago.clag.converter.Converter;
import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;
import com.giago.clag.model.Options;
import com.giago.clag.provider.Provider;
import com.giago.clag.util.RequestWrapper;


/**
 * @author luigi.agosti
 */
public class GaeRestContext extends RequestWrapper implements Context {
	
	public static final String CLIENT_ID = "_id";
	
	private static final String SEPARATOR = "/";
	
	private static final String QUESTION_MARK = "?";
	
	private Provider provider;
	
	private Converter converter;
	
	private ServiceInfo serviceInfo;
	
	public GaeRestContext() {
	}
	
	public GaeRestContext(Map<String, String[]> request) {
		super(request);
	}
	
	public GaeRestContext(Map<String, String[]> request, Long etag) {
		super(request, etag);
	}
	
	public GaeRestContext(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public void setRequest(HttpServletRequest request) {
		super.setRequest(request);
	}
	
	@Override
	public void setUri(String uri) {
		super.setUri(uri);
	}

	@Override
	public boolean isQuery() {
		return getParameterAsBooleanIfContained(Parameter.QUERY, true);
	}

	@Override
	public boolean isSchema() {
		return getParameterAsBooleanIfContained(Parameter.SCHEMA, false);
	}
	
	@Override
	public String getName() {
		String uri = getUri();
		if(uri != null) {
			int index = uri.lastIndexOf(SEPARATOR);
			int terminalIndex = uri.lastIndexOf(QUESTION_MARK);
			if(index > 0 && index + 1 < uri.length()) {
				if(terminalIndex>0) {
					return uri.substring(index + 1, terminalIndex);
				}
				return uri.substring(index + 1);
			}
		}
		return null;
	}

	@Override
	public String getSortOrder() {
		return getParameterAsString(Parameter.SORT_ORDER);
	}

	@Override
	public String[] getSelectionArgs() {
		return getParameterAsStringArray(Parameter.SELECTION_ARGS);
	}

	@Override
	public String getSelection() {
		return getParameterAsString(Parameter.SELECTION);
	}

	@Override
	public String[] getProjection() {
		return getParameterAsStringArray(Parameter.PROJECTION);
	}
	
	@Override
	public String getRemoteId() {
		return getParameterAsString(Parameter.REMOTE_ID);
	}

	@Override
	public Converter getConverter() {
		return converter;
	}

	@Override
	public Provider getProvider() {
		return provider;
	}

	@Override
	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	@Override
	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	@Override
	public Options getFetchOptions() {
		Options dl = new Options();
		if(etag != null) {
			dl.setEtag(etag);
		} else {
			dl.setEtag(Options.DEFAULT_ETAG);
		}
		dl.setLimit(getParameterAsInt(Parameter.LIMIT, Options.DEFAULT_LIMIT));
		dl.setOffset(getParameterAsInt(Parameter.OFFSET, Options.DEFAULT_OFFSET));
		return dl;
	}

	@Override
	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	@Override
	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}
	
	@Override
	public Cursor getCursorFromRequest(MetaEntity me) {
		return new Cursor(me.getName(), getEntity(me));
	}

	public Map<String, Object> getEntity(MetaEntity me) {
		Map<String, Object> map = new HashMap<String, Object>();
		for(String key : me.getPropertyNames()) {
			String value = getParameterAsString(key);
			if(value != null){
				map.put(key, value);
			}
		}
		return map;
	}
	
	@Override
	public Cursor getCursorFromJsonDataRequest(MetaEntity me) throws JsonParseException, JsonMappingException, IOException {
		String data = getData();
		Cursor c = new Cursor(me.getName());
		if(data != null && data.length() > 0) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readValue(data, JsonNode.class);
            Iterator<JsonNode> array = rootNode.getElements();
            while(array.hasNext()) {
            	JsonNode node = array.next();
            	for(String property : me.getPropertyNames()) {
            		if(!property.equals(me.getKeyProperty())) {
            			MetaProperty mp = me.getMetaProperty(property);
            			JsonNode attributeNode = node.get(property);
            			if(attributeNode != null) {
            				if(mp.getClazz() == Integer.class) {
            					String value = attributeNode.getValueAsText();
            					if(value != null) {
            						c.add(property, new Integer(value));
            					}
            				} else if(mp.getClazz() == Date.class) {
            					String value = attributeNode.getValueAsText();
            					if(value != null) {
            						c.add(property, new Date(new Long(value)));
            					}
            				} else if(mp.getClazz() == String.class) {
            					String value = attributeNode.getValueAsText();
            					if(value != null) {
            						c.add(property, value);
            					}
            				} else if(mp.getClazz() == Boolean.class) {
            					Boolean value = attributeNode.getBooleanValue();
            					if(value != null) {
            						c.add(property, value);
            					}
            				} else if(mp.getClazz() == Long.class) {
            					String value = attributeNode.getValueAsText();
            					if(value != null) {
            						c.add(property, new Long(value));
            					}
            				}
            			}
            		}
            	}
            	JsonNode attributeNode = node.get(CLIENT_ID);
            	if(attributeNode != null) {
            		String value = attributeNode.getValueAsText();
            		if(value != null) {
						c.add(CLIENT_ID, new Long(value));
					}
            	}
            	c.next();
            }
		}	
		return c;
	}

}
