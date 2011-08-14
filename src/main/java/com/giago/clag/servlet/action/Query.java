package com.giago.clag.servlet.action;

import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.servlet.context.Context;


public class Query implements Action {

	@Override
	public String execute(Context context) {
		MetaEntity mds = context.getProvider().schema(context.getName());
		
		Cursor cursor = context.getProvider().query(context.getName(),
				context.getProjection(), context.getSelection(),
				context.getSelectionArgs(), context.getSortOrder(), mds,
				context.getFetchOptions());
		return context.getConverter().convert(cursor, mds, context);
	}

}
