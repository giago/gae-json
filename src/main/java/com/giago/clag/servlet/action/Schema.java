package com.giago.clag.servlet.action;

import com.giago.clag.model.MetaEntity;
import com.giago.clag.servlet.context.Context;


public class Schema implements Action {

	@Override
	public String execute(Context context) {
		MetaEntity mds = context.getProvider().schema(context.getName());
		return context.getConverter().convert(mds, context);
	}

}
