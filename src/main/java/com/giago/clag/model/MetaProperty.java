package com.giago.clag.model;

import com.giago.clag.model.MetaEntity.OnConflictPolicy;

/**
 * Property is the atomic piece of information that is forming an entity.
 * 
 * @author luigi.agosti
 * 
 */
public class MetaProperty {

	private String name;

	private String type;
	
	private Class<?> clazz;

	private boolean key;

	private boolean canBeNull;
	
	private boolean isRelation;
	
	private boolean unique;
	
	private boolean include;
	
	private String owner;
	
	private String parent;
	
	private String child;
	
	private String from;
	
	private boolean filterUserId;
	
	private boolean persistUserId;
	
	private boolean userIds;
	
	private boolean email;
	
	private boolean etag;
	
	private OnConflictPolicy onConflictPolicy;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}
	
	public boolean getUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isCanBeNull() {
		return canBeNull;
	}

	public void setCanBeNull(boolean canBeNull) {
		this.canBeNull = canBeNull;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParent() {
		return parent;
	}
	
	public void setChild(String child) {
		this.child = child;
	}

	public String getChild() {
		return child;
	}
	
	public boolean isParent() {
		if(parent != null) {
			return true;
		}
		return false;
	}
	
	public boolean isChild() {
		if(child != null) {
			return true;
		}
		return false;
	}


	public void setRelation(boolean isRelation) {
		this.isRelation = isRelation;
	}

	public boolean isRelation() {
		return isRelation;
	}


	public void setFrom(String from) {
		this.from = from;
	}

	public String getFrom() {
		return from;
	}


	public void setInclude(boolean include) {
		this.include = include;
	}

	public boolean isInclude() {
		return include;
	}
	
	public void setFilterUserId(boolean filterUserId) {
		this.filterUserId = filterUserId;
	}

	public boolean getFilterUserId() {
		return filterUserId;
	}
	
	public boolean getPersistUserId() {
		return persistUserId;
	}
	
	public void setPersistUserId(boolean persistUserId) {
		this.persistUserId = persistUserId;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}
	
	public void setOnConflictPolicy(OnConflictPolicy onConflictPolicy) {
		this.onConflictPolicy = onConflictPolicy;
	}

	public OnConflictPolicy getOnConflictPolicy() {
		return onConflictPolicy;
	}
	
	public boolean hasConflictPolicy() {
		if(onConflictPolicy == null) {
			return false;
		}
		return true;
	}


	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}


	public void setEmail(boolean email) {
		this.email = email;
	}

	public boolean isEmail() {
		return email;
	}
	
	public void setUserIds(boolean userIds) {
		this.userIds = userIds;
	}

	public boolean isUserIds() {
		return userIds;
	}

	public boolean isEtag() {
		return etag;
	}

	public void setEtag(boolean etag) {
		this.etag = etag;
	}
	/**
	 * Builder
	 */
	public static final class Builder {
		private MetaProperty md;

		public Builder(String name) {
			md = new MetaProperty();
			md.setName(name);
		}

		public Builder type(Class<?> clazz, String type) {
			md.setType(type);
			md.setClazz(clazz);
			return this;
		}
		
		public Builder clazz(Class<?> clazz){
			md.setClazz(clazz);
			md.setType(MetaEntity.getType(clazz));
			return this;
		}
		
		
		public Builder parent(String parent) {
			md.setParent(parent);
			return this;
		}

		public Builder key(boolean key) {
			md.setKey(key);
			return this;
		}

		public Builder canBeNull(boolean canBeNull) {
			md.setCanBeNull(canBeNull);
			return this;
		}
		
		public Builder child(String property) {
			md.setChild(property);
			return this;
		}

		public MetaProperty build() {
			return md;
		}

		public Builder isRelation(boolean isRelation, String from, boolean include) {
			md.setRelation(true);
			md.setFrom(from);
			md.setInclude(include);
			return this;
		}

		public Builder owner(String owner) {
			md.setOwner(owner);
			return this;
		}

		public Builder unique(boolean unique) {
			md.setUnique(unique);
			return this;
		}

		public Builder persistUserId(boolean persistUserId) {
			md.setPersistUserId(persistUserId);
			return this;
		}
		
		public Builder filterUserId(boolean filterUserId) {
			md.setFilterUserId(filterUserId);
			return this;
		}
		
		public Builder email(boolean email) {
			md.setEmail(email);
			return this;
		}
		
		public Builder userIds(boolean userIds) {
			md.setUserIds(userIds);
			return this;
		}
		
		public Builder onConflictPolicy(OnConflictPolicy onConflictPolicy) {
			md.setOnConflictPolicy(onConflictPolicy);
			return this;
		}

		public Builder etag(boolean etag) {
			md.setEtag(etag);
			return this;
		}
	}

}
