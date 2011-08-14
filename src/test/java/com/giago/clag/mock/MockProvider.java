package com.giago.clag.mock;

import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.Options;
import com.giago.clag.provider.AbstractProvider;


/**
 * @author luigi.agosti
 */
public class MockProvider extends AbstractProvider {

	@Override
	public Cursor query(String name, String[] projection, String selection,
			String[] selectionArgs, String sortOrder, MetaEntity entity) {
		return null;
	}

	@Override
	public MetaEntity schema(String name) {
		return null;
	}

	@Override
	public Cursor query(String name, String[] projection, String selection,
			String[] selectionArgs, String sortOrder, MetaEntity entity,
			Options dataLimitation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor insert(String name, Cursor values, MetaEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String name, String remoteId, MetaEntity mds) {
		// TODO Auto-generated method stub
	}
	
	

}
