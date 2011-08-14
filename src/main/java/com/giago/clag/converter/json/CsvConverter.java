package com.giago.clag.converter.json;

import java.util.Date;
import java.util.Map;

import com.giago.clag.model.Cursor;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.servlet.context.Context;



public class CsvConverter extends BaseConverter {

	private static final String CSV_CONTENT = "text/csv";
	
	@Override
	public String convert(MetaEntity mds, Context context) {
		return "";
	}

	@Override
	public String convert(Cursor cursor, MetaEntity mds, Context context) {
		try {
			StringBuilder sb = new StringBuilder();
			convert(sb, cursor, context);
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void convert(StringBuilder sb, Cursor cursor, Context context) {
		for (Map<String, Object> row : cursor.getRows()) {
			for (String key : row.keySet()) {
				Object obj = row.get(key);
				if (obj instanceof Date) {
					sb.append((((Date) obj).getTime()));
				} else {
					String value = "" + row.get(key);
					value = value.replaceAll(",", " ");
					sb.append(value);
				}
				sb.append(",");
			}
			sb.replace(sb.length()-1, sb.length(), "");
			sb.append("\n");
		}
	}

	@Override
	public String convertIdsOnly(Cursor cursor, MetaEntity mds, Context context) {
		return "";
	}

	@Override
	public String describe(Context context) {
		return "";
	}
	
	@Override
	public String getContentType() {
		return CSV_CONTENT;
	}

}
