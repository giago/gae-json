package com.giago.clag.introspector.jdo;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.giago.clag.introspector.AbstractIntrospector;
import com.giago.clag.introspector.annotation.Clag;
import com.giago.clag.model.MetaEntity;
import com.giago.clag.model.MetaProperty;


/**
 * @author luigi.agosti
 */
public class JdoIntrospector extends AbstractIntrospector {

	private static final Logger logger = Logger
			.getLogger(JdoIntrospector.class.getName());

	@Override
	protected void filterFields(Field field, MetaEntity mds) {
		if (field.getAnnotation(Clag.class) != null) {
			logger.info("Adding field key : " + field.getName());
			Clag c = (Clag) field.getAnnotation(Clag.class);
			if(c.from().length() > 0) {
				logger.info("Adding key for relation : " + field.getName());
				mds.addRelation(field.getName(), mds.getName(), c.from(),
						field.getType(), c.include());
			} else {
				logger.info("Adding key for clag property : " + field.getName());
				mds.add(new MetaProperty.Builder(field.getName()).key(c.key())
					.unique(c.unique()).onConflictPolicy(c.onConflictPolicy())
					.clazz(field.getType()).filterUserId(c.filterUserId())
					.persistUserId(c.persistUserId()).userIds(c.userIds())
					.email(c.email()).etag(c.etag()).build());
			}
		} else if (field.getAnnotation(PrimaryKey.class) != null) {
			logger.info("Adding field key : " + field.getName());
			mds.addKey(field.getName(), field.getType());
		} else if (field.getAnnotation(Persistent.class) != null) {
			logger.info("Adding field : " + field.getName());
			mds.add(field.getName(), field.getType());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected MetaEntity analyseClass(Class clazz) {
		// put here the code if in the future will be necessary to use class
		// annotation
		return new MetaEntity(clazz.getName(), clazz.getSimpleName());
	}
}
