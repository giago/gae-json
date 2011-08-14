package com.giago.clag.provider.gae;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

import com.google.appengine.api.datastore.Query;

/**
 * @author luigi.agosti
 */
public class GaeQueryHelperTest {
	
	private GaeQueryHelper queryHelper = new GaeQueryHelper();

	//==================================================
	//   SORT
	//==================================================
	
	@Test
	public void getSortsWhenNoValue() {
		assertTrue(queryHelper.getSorts(null).isEmpty());
		assertTrue(queryHelper.getSorts("").isEmpty());
	}
	
	@Test
	public void getOrderDirectionBatteryOfTests() {
		assertEquals(Query.SortDirection.DESCENDING, queryHelper.getSorts("property desc").get(0).direction);
		assertEquals(Query.SortDirection.DESCENDING, queryHelper.getSorts("property Desc").get(0).direction);
		assertEquals(Query.SortDirection.ASCENDING, queryHelper.getSorts("property asc").get(0).direction);
		assertEquals(Query.SortDirection.ASCENDING, queryHelper.getSorts("property").get(0).direction);
	}

	@Test
	public void getOrderPropertiesBatteryOfTests() {
		assertEquals("property", queryHelper.getSorts("property desc").get(0).propertyName);
		assertEquals("property", queryHelper.getSorts("property asc").get(0).propertyName);
		assertEquals("property", queryHelper.getSorts("property").get(0).propertyName);		
	}

	@Test
	public void getNumberOfSortsReckognition() {		
		assertEquals(1, queryHelper.getSorts("property desc").size());
		assertEquals(2, queryHelper.getSorts("property1 desc, property2 asc").size());
	}

	@Test
	public void getOrderWithMultipleProperty() {
		assertEquals("property1", queryHelper.getSorts("property1 desc, property2 asc").get(0).propertyName);
		assertEquals(Query.SortDirection.DESCENDING, queryHelper.getSorts("property1 desc, property2 asc").get(0).direction);
		assertEquals("property2", queryHelper.getSorts("property1 desc, property2 asc").get(1).propertyName);
		assertEquals(Query.SortDirection.ASCENDING, queryHelper.getSorts("property1 desc, property2 asc").get(1).direction);
	}
		
		
	@Test
	public void getStrangeCasesOfSortOrders() {
		assertEquals("property1", queryHelper.getSorts("property1, property2 asc").get(0).propertyName);
		assertEquals(Query.SortDirection.ASCENDING, queryHelper.getSorts("property1, property2 asc").get(0).direction);
		assertEquals("property2", queryHelper.getSorts("property1, property2 asc").get(1).propertyName);
		assertEquals(Query.SortDirection.ASCENDING, queryHelper.getSorts("property1, property2 asc").get(1).direction);
		
		assertEquals("property1", queryHelper.getSorts("property1,property2 desc").get(0).propertyName);		
		assertEquals(Query.SortDirection.ASCENDING, queryHelper.getSorts("property1,property2 desc").get(0).direction);		
		assertEquals("property2", queryHelper.getSorts("property1,property2 desc").get(1).propertyName);		
		assertEquals(Query.SortDirection.DESCENDING, queryHelper.getSorts("property1,property2 desc").get(1).direction);	
	}
	
	//==================================================
	//   Filter and Filter Args
	//==================================================
	
	@Test
	public void getFiltersWithNoValue() {
		assertTrue(queryHelper.getFilters(null, null).isEmpty());
		assertTrue(queryHelper.getFilters("", null).isEmpty());
		assertTrue(queryHelper.getFilters(null, new String[]{}).isEmpty());
		assertTrue(queryHelper.getFilters("", new String[]{}).isEmpty());
	}
	
	@Test
	public void getNumberOfFiltersReckognition() {		
		assertEquals(1, queryHelper.getFilters("property = \"x\"", null).size());
		assertEquals(2, queryHelper.getFilters("property1 = \"x\", property2 = \"asc\"", null).size());
	}

	@Test
	public void getFiltersSimpleComparationWithConstant() {
		assertEquals(Query.FilterOperator.EQUAL, queryHelper.getFilters("property1 = \"xx\"", null).get(0).operator);
		assertEquals("xx", queryHelper.getFilters("property1 = \"xx\"", null).get(0).value);
		assertEquals("property1", queryHelper.getFilters("property1 = \"xx\"", null).get(0).propertyName);
	}

	@Test
	public void getFiltersMultipleComparationWithConstant() {
		assertEquals(Query.FilterOperator.EQUAL, queryHelper.getFilters("property1 = \"xx\"", null).get(0).operator);
		assertEquals("xx", queryHelper.getFilters("property1 = \"xx\"", null).get(0).value);
		assertEquals("property1", queryHelper.getFilters("property1 = \"xx\"", null).get(0).propertyName);
	}

	@Test(expected = RuntimeException.class)
	public void getNotSupportedOperator() {
		queryHelper.getFilters("property1 != \"xx\"", null);
	}

	@Test(expected = RuntimeException.class)
	public void getNotSupportedValue() {
		queryHelper.getFilters("property1 != xx", null);
	}

	@Test(expected = RuntimeException.class)
	public void getNotSupportedFilter() {
		queryHelper.getFilters("property1 12", null);
	}
	
	@Test(expected = RuntimeException.class)
	public void getFiltersSimpleComparationWithQuestionMarkButNoSelectionArgs() {
		queryHelper.getFilters("property1 = ?", null);
	}

	@Test
	public void getFiltersComparationWithQuestionMark() {
		assertEquals("xx", queryHelper.getFilters("property1 = ?", new String[]{"xx"}).get(0).value);
		assertEquals("x1", queryHelper.getFilters("property1 = ?, property2 = ?", new String[]{"x1", "x2"}).get(0).value);
		assertEquals("x2", queryHelper.getFilters("property1 = ?, property2 = ?", new String[]{"x1", "x2"}).get(1).value);
	}
}
