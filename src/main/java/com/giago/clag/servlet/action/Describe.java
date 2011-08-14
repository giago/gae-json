package com.giago.clag.servlet.action;

import com.giago.clag.servlet.context.Context;

public class Describe implements Action {

	@Override
	public String execute(Context context) {
		return context.getConverter().describe(context);
	}

}
