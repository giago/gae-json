package com.giago.clag.converter;

import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.servlet.context.Context;


/**
 * @author luigi.agosti
 */
public interface Converter {

	/**
	 * Convert a entity in a format readable for the user.
	 * @param mds
	 * @param context
	 * @return
	 */
	String convert(MetaEntity mds, Context context);

	/**
	 * A list of result organized in a cursor in a format readable for the user.
	 * @param cursor
	 * @param mds
	 * @param context
	 * @return
	 */
	String convert(Cursor cursor, MetaEntity mds, Context context);

	/**
	 * Return the all description of the schema.
	 * @param context
	 * @return
	 */
	String describe(Context context);

	/**
	 * Convert only the ids and generate a minimal json useful to update verify
	 * and keep remote ids.
	 * @param cursor
	 * @param mds
	 * @param context
	 * @return
	 */
	String convertIdsOnly(Cursor cursor, MetaEntity mds, Context context);

	/**
	 * Retrieve the type of content.
	 */
	String getContentType();

}
