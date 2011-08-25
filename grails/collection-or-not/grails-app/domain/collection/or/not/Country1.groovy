package collection.or.not

class Country1 {

	String name

	static hasMany = [ citizens : Citizen1 ]

	static mapping = {
		sort "name"
		citizens sort:"name"
	}

	static constraints = {
		name nullable:false, blank:false, unique:true
	}

	String toString() {
		"Country1 $name"
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this.is(obj))
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Country1))
			return false;
		Country1 other = (Country1) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
