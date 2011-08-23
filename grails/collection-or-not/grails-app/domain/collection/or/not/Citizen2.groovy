package collection.or.not

class Citizen2 {

	String name
	Integer age
	Country2 country

	static mapping = { sort "name" }

	static constraints = {
		name nullable:false, blank:false, unique:['country']
		age nullable:false
	}

	String toString() {
		"$name $age"
	}
}
