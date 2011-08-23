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
}
