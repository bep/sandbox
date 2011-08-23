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
}
