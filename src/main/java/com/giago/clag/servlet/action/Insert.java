package com.giago.clag.servlet.action;

import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.servlet.context.Context;


public class Insert implements Action {
	
	@Override
	public String execute(Context context) {
		MetaEntity mds = context.getProvider().schema(context.getName());
		try {
			Cursor cursor = context.getProvider().insert(context.getName(), context.getCursorFromJsonDataRequest(mds), mds);
			return context.getConverter().convertIdsOnly(cursor, mds, context);
		} catch (Exception e) {
			throw new RuntimeException("ummm", e);
		}
	}

}
