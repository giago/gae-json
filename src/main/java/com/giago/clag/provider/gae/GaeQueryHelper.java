package com.giago.clag.provider.gae;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * @author luigi.agosti
 */
public class GaeQueryHelper {

	private static final Query.SortDirection DEFAULT_DIRECTION_ORDER = Query.SortDirection.ASCENDING;
	
	private static final String DESC = "desc";

	private static final String ASC = "asc";

	private static final String SPACE_SEPARATOR = " ";

	private static final String COMMA_SEPARATOR = ",";

	private static final String EMPTY = "";
	
	private static final String SQL_EQUAL_OPERATOR = "=";

	private static final String QUESTION_MARK_WITH_ESCAPE = "\\?";
	
	public class FilterHolder {
		public String propertyName;
		public Query.FilterOperator operator;
		public Object value;
	}

	public class SortHolder {
		public String propertyName;
		public Query.SortDirection direction;
	}
	
	public List<SortHolder> getSorts(String sortOrder) {
		List<SortHolder> shs = new ArrayList<SortHolder>();
		if(sortOrder == null || EMPTY.equals(sortOrder)) {
			return shs;
		}
		List<String> sorters = Arrays.asList(sortOrder.split(COMMA_SEPARATOR));
		for(String sortToken : sorters) {
			List<String> sort = Arrays.asList(sortToken.trim().split(SPACE_SEPARATOR));
			SortHolder sh = new SortHolder();
			for(String token : sort) {
				sh.direction = DEFAULT_DIRECTION_ORDER;
				if(DESC.equalsIgnoreCase(token)) {
					sh.direction = Query.SortDirection.DESCENDING;				
				} else if(!ASC.equalsIgnoreCase(token)) {
					sh.propertyName = token;
				}
			}
			shs.add(sh);
		}
		return shs;
	}
	
	public List<FilterHolder> getFilters(String selection, String[] selectionArgs){
		List<FilterHolder> fhs = new ArrayList<FilterHolder>();
		if(selection == null || EMPTY.equals(selection)) {
			return fhs;
		}
		selection = populate(selection, selectionArgs);
		List<String> filtersTokens = Arrays.asList(selection.split(COMMA_SEPARATOR));
		for(String filterToken : filtersTokens) {
			List<String> tokens = Arrays.asList(filterToken.trim().split(SPACE_SEPARATOR));
			if(tokens.size() == 3) {
				FilterHolder fh = new FilterHolder();
				fh.propertyName = tokens.get(0);
				fh.operator = getOperator(tokens.get(1)); 
				fh.value = getObject(tokens.get(2)); 
				fhs.add(fh);
			} else {
				throw new RuntimeException("Filter " + filterToken + " not supported");
			}
		}
		return fhs;
	}

	private Object getObject(String value) {
		if(value.length() > 2 && value.startsWith("\"") && value.endsWith("\"")) {
			return value.substring(1, value.length() -1);
		} else if(value.indexOf("\"") < 0) {
			try {
				return Integer.valueOf(value);
			} catch (Exception e) {
				throw new RuntimeException("Value for filter " + value + " not supported");
			}
		} else {
			throw new RuntimeException("Value for filter " + value + " not supported");
		}
	}

	private FilterOperator getOperator(String operator) {
		if(SQL_EQUAL_OPERATOR.equals(operator)) {
			return FilterOperator.EQUAL;
		} else {
			throw new RuntimeException("Operator " + operator + " not supported");
		}
	}
	
	private String populate(String selection, String[] selectionArgs) {
		if(selectionArgs == null || selectionArgs.length == 0) {
			return selection;
		}
		for(String selectionArg : selectionArgs) {
			selection = selection.replaceFirst(QUESTION_MARK_WITH_ESCAPE, "\"" + selectionArg + "\"");
		}
		return selection;
	}
	
}
