package com.giago.clag.servlet.config;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;


import com.giago.clag.converter.Converter;
import com.giago.clag.converter.json.RestProviderConverter;
import com.giago.clag.introspector.Introspector;
import com.giago.clag.introspector.jdo.JdoIntrospector;
import com.giago.clag.provider.Provider;
import com.giago.clag.provider.gae.GaeProvider;
import com.giago.clag.servlet.context.Context;
import com.giago.clag.servlet.context.GaeRestContext;
import com.giago.clag.servlet.context.ServiceInfo;
import com.google.appengine.api.utils.SystemProperty;

@SuppressWarnings("unchecked")
public class GaeServletConfigurator implements Configurator {

	private static final Logger logger = Logger.getLogger(Configurator.class.getName());
	
	private static final String DEFAULT_CONTEXT = GaeRestContext.class.getName();

	private static final String DEFAULT_PROVIDER = GaeProvider.class.getName();

	private static final String DEFAULT_CONVERTER = RestProviderConverter.class.getName();

	private static final String DEFAULT_INTROSPECTOR = JdoIntrospector.class.getName();

	private static final String COMMA = ",";
	
	private Class contextClass;
	
	
	private Provider provider;
	
	private Converter converter;
	
	public GaeServletConfigurator(ServletConfig config) {
		init(config);
	}
	
	private String getInitParameter(ServletConfig config, String key, String defaultValue) {
		String converterParam = config.getInitParameter(key);
		if(converterParam == null || converterParam.length() <= 0) {
			logger.info("Parameter " + key + " not defined, using default : " + defaultValue);
			converterParam = defaultValue;
		}
		return converterParam;
	}
	
	private void init(ServletConfig config) {
		logger.info("Clag Servlet : initialization");
		String converterParam = getInitParameter(config, InitParameters.CONVERTER, DEFAULT_CONVERTER);
		String providerParam = getInitParameter(config, InitParameters.CONTENT_PROVICER, DEFAULT_PROVIDER);
		String introspectorParam = getInitParameter(config, InitParameters.INTROSPECTOR, DEFAULT_INTROSPECTOR);
		String contextParam = getInitParameter(config, InitParameters.CONTEXT, DEFAULT_CONTEXT);
		String contentClassesParam = config.getInitParameter(InitParameters.CONTENT_CLASSES);
		logger.info("context : " + contextParam);
		logger.info("converter : " + converterParam);
		logger.info("provicer : " + providerParam);
		logger.info("introspector : " + introspectorParam);
		logger.info("content classes : " + contentClassesParam);
		try {
			if(contextParam == null) {
				logger.info("Context class is not defined, using default : " + DEFAULT_CONTEXT);
				contextParam = DEFAULT_CONTEXT;
			}
			contextClass = (Class<Context>)Class.forName(contextParam);

			if(converterParam == null) {
				logger.info("Converter class is not defined, using default : " + DEFAULT_CONVERTER);
				converterParam = DEFAULT_PROVIDER;
			}
			converter = (Converter)Class.forName(converterParam).newInstance();

			if(providerParam == null) {
				logger.info("Provider class is not defined, using default : " + DEFAULT_PROVIDER);
				providerParam = DEFAULT_PROVIDER;
			}
			provider = (Provider)Class.forName(providerParam).newInstance();
			
			if(introspectorParam == null) {
				logger.info("Introspector is not defined, using default : " + DEFAULT_INTROSPECTOR);
				introspectorParam = DEFAULT_INTROSPECTOR;
			}
			provider.setIntrospector((Introspector)Class.forName(introspectorParam).newInstance());
			
			List<String> classes = Arrays.asList(contentClassesParam.split(COMMA));
			for(String clazz : classes) {
				logger.info("adding class to content provider : " + clazz);
				provider.add(Class.forName(clazz));
			}
			provider.linkMetaEntities();
			isConfigured();
		} catch (Exception e) {
			logger.severe("Problem in the initialization, see the following stack trace : " + e.getMessage());
		}
	}

	public Provider getProvider() {
		return provider;
	}

	public Converter getConverter() {
		return converter;
	}
	
	@Override
	public void isConfigured() {
		provider.isConfigured();
	}

	@Override
	public Context getContext() {
		try {
			Context context =  (Context)contextClass.newInstance();
			context.setProvider(provider);
			context.setConverter(converter);
			return context;
		} catch (InstantiationException e) {
			logger.severe("InstantiationException while getting interpreter instance : " + e.getMessage());
			throw new RuntimeException("Interpreter is wrong");
		} catch (IllegalAccessException e) {
			logger.severe("IllegalAccessException while getting interpreter instance : " + e.getMessage());
			throw new RuntimeException("Interpreter is wrong");
		}
	}

	@Override
	public ServiceInfo getServiceInfo() {
		ServiceInfo info = new ServiceInfo();
		info.setName(SystemProperty.applicationId.get());
		info.setVersion(SystemProperty.applicationVersion.get());
		return info;
	}
	
}
