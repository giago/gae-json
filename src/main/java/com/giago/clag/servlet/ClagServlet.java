package com.giago.clag.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.giago.clag.converter.json.BaseConverter;
import com.giago.clag.servlet.action.Delete;
import com.giago.clag.servlet.action.Describe;
import com.giago.clag.servlet.action.Insert;
import com.giago.clag.servlet.action.Query;
import com.giago.clag.servlet.action.Schema;
import com.giago.clag.servlet.config.Configurator;
import com.giago.clag.servlet.config.GaeServletConfigurator;
import com.giago.clag.servlet.context.Context;


/**
 * Restful servlet that handle all the requests.
 * It then redirect to the correct action, collect the result and 
 * send it back through the response.
 * 
 * @author luigi.agosti
 */
public class ClagServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ClagServlet.class.getName());

	private Configurator configurator;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		configurator = new GaeServletConfigurator(config);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("Executing get request for : " + req.getRequestURI());
		Context context = configurator.getContext();
		context.setRequest(req);
		String result = null;
		String name = context.getName();
		if(name != null && name.length() > 0){
			if (context.isSchema()) {
				logger.info("Executing schema");
				result = new Schema().execute(context);
			} else if (context.isQuery()) {
				logger.info("Executing query");
				result = new Query().execute(context);
			} else {
				throw new RuntimeException("No action implemented for " + name);
			}
		} else {			
			logger.info("Executing describe");
			context.setServiceInfo(configurator.getServiceInfo());
			result = new Describe().execute(context);
		}
		logger.info("request executed, sending back the result");
		resp.setContentType(getContentType(context));
		resp.setContentLength(result.length());
		PrintWriter out = resp.getWriter();
		out.println(result);
		out.close();
		out.flush();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("Executing post request for : " + req.getRequestURI());
		Context context = configurator.getContext();
		context.setRequest(req);
		String name = context.getName();
		if(name != null && name.length() > 0){
			String result = null;
			result = new Insert().execute(context);	
			logger.info("request executed, sending back the result");
			resp.setContentType(getContentType(context));
			resp.setContentLength(result.length());
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.close();
			out.flush();
		} else {
			throw new RuntimeException("No action implemented for " + name);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("Executing delete request for : " + req.getRequestURI());
		Context context = configurator.getContext();
		context.setRequest(req);
		String name = context.getName();
		if(name != null && name.length() > 0){
			String result = null;
			result = new Delete().execute(context);
			logger.info("request executed, sending back the result");
			resp.setContentType(getContentType(context));
			resp.setContentLength(result.length());
			PrintWriter out = resp.getWriter();
			out.println(result);
			out.close();
			out.flush();
		} else {
			throw new RuntimeException("No action implemented for " + name);
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("Executing put request for : " + req.getRequestURI());
		// TODO update or replace
		throw new RuntimeException("Put not implemented yet!");
	}
	
	private String getContentType(Context context) {
		if(context == null || context.getConverter() == null || context.getConverter().getContentType() == null) {
			return BaseConverter.CONTENT_TYPE; 
		}
		return context.getConverter().getContentType();
	}

}
