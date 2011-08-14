
package com.giago.clag.introspector;

import java.util.Map;

import com.giago.clag.model.MetaEntity;


/**
 * @author luigi.agosti
 */
public interface Introspector {

	/**
	 * Return a map that contains the definition of columns and types
	 *  
	 * @return
	 */
	MetaEntity extractMetaEntity(@SuppressWarnings("unchecked") Class classToParse);
	
	/**
	 * Is important to run the liking after every meta entity 
	 * has been read because in this way the linkage is setting all
	 * the relationship correctly duplicating them to make the life of
	 * the users of the meta entities easier
	 * 
	 * @param metaEntities
	 */
	void linking(Map<String, MetaEntity> metaEntities);

}
