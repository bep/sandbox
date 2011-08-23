package collection.or.not

class Citizen1 {

	String name
	Integer age

	static belongsTo = [ country : Country1 ]

	static mapping = { sort "name" }

	static constraints = {
		name nullable:false, blank:false, unique:['country']
		age nullable:false
	}

	String toString() {
		"$name $age"
	}
}
