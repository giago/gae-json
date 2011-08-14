package com.giago.clag.servlet.config;

import com.giago.clag.servlet.context.Context;
import com.giago.clag.servlet.context.ServiceInfo;
import com.giago.clag.util.Configurable;


public interface Configurator extends Configurable {

	interface InitParameters {
		String CONVERTER = "converter";
		
		String CONTENT_PROVICER = "provider";
		
		String INTROSPECTOR = "introspector";
		
		String CONTENT_CLASSES = "contentClasses";
		
		String CONTEXT = "contextClass";
	}

	Context getContext();

	ServiceInfo getServiceInfo();

}
