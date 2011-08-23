import org.springframework.util.StopWatch;

import collection.or.not.*;

class BootStrap {
	def static final int NUM_CITIZENS = 10000
	def init = { servletContext ->

		def stopwatch = new StopWatch()

		stopwatch.start("Creating Japan With Collection")
		Country1 country1 = new Country1(name: "Japan With Collection").save(flush:true, failOnError:true)
		stopwatch.stop()
		stopwatch.start("Adding the first $NUM_CITIZENS citizens to Japan With Collection")
		(1..NUM_CITIZENS).each {
			country1.addToCitizens(new Citizen1(name:"Citizen 1 $it", age: (it * 10)))
		}
		country1.save(flush:true, failOnError:true)
		stopwatch.stop()
		stopwatch.start("Adding one more citizen to Japan With Collection")
		country1.addToCitizens(new Citizen1(name:"Citizen next", age: 100))
		country1.save(flush:true, failOnError:true)
		stopwatch.stop()
		stopwatch.start("Creating Japan Without Collection")
		Country2 country2 = new Country2(name: "Japan Without Collection").save(flush:true, failOnError:true)
		stopwatch.stop()
		stopwatch.start("Adding the first $NUM_CITIZENS citizens to Japan Without Collection")
		(1..NUM_CITIZENS).each {
			new Citizen2(name:"Citizen 2 $it", age: (it * 10), country: country2).save(flush:true, failOnError:true)
		}
		stopwatch.stop()
		stopwatch.start("Adding one more citizen to Japan Without Collection")
		new Citizen2(name:"Citizen 2 next", age: 100, country: country2).save(flush:true, failOnError:true)
		stopwatch.stop()
		println(stopwatch.prettyPrint())
	}
	def destroy = {
	}
}
