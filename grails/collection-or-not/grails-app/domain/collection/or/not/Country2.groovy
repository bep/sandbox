package collection.or.not

class Country2 {

	String name

	static mapping = { sort "name" }

	static constraints = {
		name nullable:false, blank:false, unique:true
	}
	
	def beforeDelete() {
		//Citizen2.withNewSession { citizens*.delete() } <--- Slow
		Citizen2.withNewSession { Citizen2.executeUpdate("delete Citizen2 where country=:country", [country: this]) }
	}
	
	def getCitizens() {
		Citizen2.findAllByCountry(this, [sort:"name"])
	}

	String toString() {
		"Country2 $name"
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Country2))
			return false;
		Country2 other = (Country2) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
