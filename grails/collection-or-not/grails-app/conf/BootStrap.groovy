import org.springframework.util.StopWatch;

import collection.or.not.*;
import org.hibernate.*

class BootStrap {
    def static final int NUM_CITIZENS = 10000
    def SessionFactory sessionFactory
    def init = { servletContext ->

        def stopWatch = new StopWatch()
        def Country1 country1 = null
        def Country2 country2 = null

        withStopWatch("Creating Japan With Collection", stopWatch) { country1 = new Country1(name: "Japan With Collection").save(flush:true, failOnError:true) }
        withStopWatch("Adding the first $NUM_CITIZENS citizens to Japan With Collection", stopWatch) {
            (1..NUM_CITIZENS).each {
                country1.addToCitizens(new Citizen1(name:"Citizen 1 $it", age: (it * 10)))
            }
        }
        withStopWatch("Adding one more citizen to Japan With Collection", stopWatch) {
            country1.addToCitizens(new Citizen1(name:"Citizen next", age: 100))
            country1.save(flush:true, failOnError:true)
        }
        withStopWatch("Creating Japan Without Collection", stopWatch) {
            country2 = new Country2(name: "Japan Without Collection").save(flush:true, failOnError:true)
        }
        withStopWatch("Adding the first $NUM_CITIZENS citizens to Japan Without Collection", stopWatch) {
            (1..NUM_CITIZENS).each {
                new Citizen2(name:"Citizen 2 $it", age: (it * 10), country: country2).save(flush:false, failOnError:true)
            }
            if(sessionFactory !=null) sessionFactory.getCurrentSession().flush()
        }
        withStopWatch("Adding one more citizen to Japan Without Collection", stopWatch) {
            new Citizen2(name:"Citizen 2 next", age: 100, country: country2).save(flush:true, failOnError:true)
        }
        withStopWatch("Deleting Japan With Collection", stopWatch) {
            Country1.withTransaction() { status ->
                // ignoring the transaction status here, rollback on exception
                Country1.findAll().each { country -> country.delete(flush:true, failOnError:true) }
            }
        }
        withStopWatch("Deleting Japan Without Collection", stopWatch) {
            Country2.withTransaction() { status ->
                // ignoring the transaction status here, rollback on exception
                Country2.findAll().each { country -> country.delete(flush:true, failOnError:true) }
            }
        }

        print(stopWatch.prettyPrint())
    }

    def withStopWatch(String title, StopWatch stopWatch, Closure c) {
        stopWatch.start(title)
        c.call()
        stopWatch.stop();
    }

    def destroy = {
    }
}
