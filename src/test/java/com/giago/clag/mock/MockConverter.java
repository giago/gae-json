package com.giago.clag.mock;

import com.giago.clag.converter.Converter;
import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.servlet.context.Context;


/**
 * @author luigi.agosti
 */
public class MockConverter implements Converter {

	@Override
	public String convert(MetaEntity entity, Context context) {
		return "schema";
	}

	@Override
	public String convert(Cursor cursor, MetaEntity entity, Context context) {
		return "query";
	}

	@Override
	public String describe(Context context) {
		return "describe";
	}

	@Override
	public String convertIdsOnly(Cursor cursor, MetaEntity mds, Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
