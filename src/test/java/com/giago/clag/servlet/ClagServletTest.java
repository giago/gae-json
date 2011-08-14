package com.giago.clag.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Hashtable;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.giago.clag.introspector.jdo.JdoIntrospector;
import com.giago.clag.introspector.jdo.sample.Story;
import com.giago.clag.mock.MockConverter;
import com.giago.clag.mock.MockProvider;
import com.giago.clag.servlet.config.Configurator;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

/**
 * @author luigi.agosti
 */
public class ClagServletTest {

	private static ServletRunner sr;
	private WebRequest request;
	private ServletUnitClient sc;

	@BeforeClass
	public static void initialSetUp() {
		Hashtable<String, String> initParameters = new Hashtable<String, String>();
		initParameters.put(Configurator.InitParameters.CONVERTER,
				MockConverter.class.getName());
		initParameters.put(Configurator.InitParameters.CONTENT_PROVICER,
				MockProvider.class.getName());
		initParameters.put(Configurator.InitParameters.INTROSPECTOR,
				JdoIntrospector.class.getName());
		initParameters.put(Configurator.InitParameters.CONTENT_CLASSES,
				Story.class.getName());

		sr = new ServletRunner();
		sr.registerServlet("data/*", ClagServlet.class.getName(),
				initParameters);
	}

	@Before
	public void setUp() {
		sc = sr.newClient();
		request = new GetMethodWebRequest(
				"http://test.meterware.com/data");
	}

	@Test
	public void shouldRespondOnQueryMethod() throws IOException, SAXException {
		request = new GetMethodWebRequest(
			"http://test.meterware.com/data/Story");
		WebResponse response = sc.getResponse(request);
		assertResponse("query", response);
	}

	@Test
	public void shouldRespondOnQueryWithoutValue() throws IOException, SAXException {
		request = new GetMethodWebRequest(
		"http://test.meterware.com/data/Story?query");
		WebResponse response = sc.getResponse(request);
		assertResponse("query", response);
	}

	@Test
	public void shouldRespondOnQueryIfSpecifiedMethodWithValue() throws IOException, SAXException {
		request = new GetMethodWebRequest(
		"http://test.meterware.com/data/Story");
		request.setParameter("query", new String[] { "true" });
		WebResponse response = sc.getResponse(request);
		assertResponse("query", response);
	}

	@Test(expected = RuntimeException.class)
	public void shouldRespondOnQueryIfSpecifiedMethodWithFalse() throws IOException, SAXException {
		request = new GetMethodWebRequest(
		"http://test.meterware.com/data/Story");
		request.setParameter("query", new String[] { "false" });
		sc.getResponse(request);
	}

	@Test
	public void shouldRespondOnSchemaMethod() throws IOException, SAXException {
		request = new GetMethodWebRequest(
			"http://test.meterware.com/data/Story");
		request.setParameter("schema", new String[] { "true" });
		WebResponse response = sc.getResponse(request);
		assertResponse("schema", response);
	}
	
	@Test
	public void shouldRespondOnSchemaMethodWithValue() throws IOException, SAXException {
		request = new GetMethodWebRequest(
		"http://test.meterware.com/data/Story?schema=true");
		WebResponse response = sc.getResponse(request);
		assertResponse("schema", response);
	}

	@Test
	public void shouldRespondOnSchemaMethodWithoutValue() throws IOException, SAXException {
		request = new GetMethodWebRequest("http://test.meterware.com/data/Story?schema");
		WebResponse response = sc.getResponse(request);
		
		assertResponse("schema", response);
	}

	@Test
	public void shouldRespondWithDescribe() throws IOException, SAXException {
		request = new GetMethodWebRequest(
		"http://test.meterware.com/data/");
		WebResponse response = sc.getResponse(request);
		
		assertResponse("describe", response);
	}

	@Ignore
	@Test
	public void shouldRespondToAPost() throws IOException, SAXException {
		request = new PostMethodWebRequest(
		"http://test.meterware.com/data/Story");
		WebResponse response = sc.getResponse(request);
		
		assertResponse("query", response);
	}
	
	private void assertResponse(String replay, WebResponse response) throws IOException {
		assertNotNull("No response received", response);
		assertEquals("content type", "text/plain", response.getContentType());
		assertEquals("requested resource", replay, response.getText());
	}

}
