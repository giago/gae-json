package com.giago.clag.introspector.annotation;

import com.giago.clag.model.MetaEntity.OnConflictPolicy;


@java.lang.annotation.Target(value={java.lang.annotation.ElementType.FIELD,java.lang.annotation.ElementType.METHOD})
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Clag {
	
	OnConflictPolicy onConflictPolicy() default OnConflictPolicy.NOT_DEFINED;
	
	boolean unique() default false;
	
	boolean key() default false;
	
	boolean searchable() default false;
	
	boolean indexable() default false;
	
	boolean hidden() default false;
	
	boolean filterUserId() default false;
	
	boolean etag() default false;
	
	boolean persistUserId() default false;
	
	boolean userIds() default false;
	
	boolean email() default false;
	
	String from() default "";
	
	boolean include() default false;
	
}
