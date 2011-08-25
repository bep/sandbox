package collection.or.not

import static org.junit.Assert.*
import grails.test.*
import groovy.lang.Closure

import org.hibernate.SessionFactory
import org.junit.AfterClass
import org.junit.BeforeClass
import org.springframework.util.StopWatch


class CollectionOrNotTests extends GroovyTestCase {
	static transactional = false

	def static final int NUM_CITIZENS = 1000
	def static final NAME_C1 = "Country With Collection of Citizens"
	def static final NAME_C2 = "Country Without Collection of Citizens"
	
	def static stopWatch = new StopWatch("CollectionOrNotTests")
	def SessionFactory sessionFactory

	@BeforeClass
	public static void  before()  {
	}

	@AfterClass
	public static void  after()  {
		print(stopWatch.prettyPrint())
	}


	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreateWithCollection() {
		withStopWatch("Creating $NAME_C1") { new Country1(name: NAME_C1).save(flush:true, failOnError:true) }
		assertTrue(Country1.count() == 1)
	}

	void testAddManyToEntityWithCollection() {
		def country1 =  Country1.findByName(NAME_C1)
		withStopWatch("Adding the first $NUM_CITIZENS citizens to Japan With Collection") {
			(1..NUM_CITIZENS).each {
				country1.addToCitizens(new Citizen1(name:"Citizen 1 $it", age: (it * 10)))
			}
			sessionFactory.getCurrentSession().flush()
		}
		assertEquals(NUM_CITIZENS, Citizen1.count())
	}

	void testAddOneMoreToEntityWithCollection() {
		def country1 =  Country1.findByName(NAME_C1)
		withStopWatch("Adding one more citizen to $NAME_C1") {
			country1.addToCitizens(new Citizen1(name:"Citizen next", age: 100))
			country1.save(flush:true, failOnError:true)
		}
		assertEquals(NUM_CITIZENS + 1, Citizen1.count())
	}

	void testCreateWithoutCollection() {
		withStopWatch("Creating $NAME_C1") {
			new Country2(name: NAME_C2).save(flush:true, failOnError:true)
		}
		assertTrue(Country2.count() == 1)
	}

	void testAddManyToEntityWithoutCollection() {
		def country2 =  Country2.findByName(NAME_C2)
		withStopWatch("Adding the first $NUM_CITIZENS citizens to $NAME_C2") {
			(1..NUM_CITIZENS).each {
				new Citizen2(name:"Citizen 2 $it", age: (it * 10), country: country2).save(flush:false, failOnError:true)
			}
			sessionFactory.getCurrentSession().flush()
		}
		assertEquals(NUM_CITIZENS, Citizen2.count())
	}

	void testAddOneMoreToEntityWithoutCollection() {
		def country2 =  Country2.findByName(NAME_C2)
		withStopWatch("Adding one more citizen to $NAME_C2") {
			new Citizen2(name:"Citizen 2 next", age: 100, country: country2).save(flush:true, failOnError:true)
		}
		assertEquals(NUM_CITIZENS + 1, Citizen2.count())
	}

	void testDeleteEntityWithCollection() {
		withStopWatch("Deleting $NAME_C1") {
			Country1.withTransaction() { status ->
				// ignoring the transaction status here, rollback on exception
				Country1.findAll().each { country -> country.delete(flush:true, failOnError:true) }
			}
		}
		assertTrue(Country1.count() == 0)
		assertTrue(Citizen1.count() == 0)

	}

	void testDeleteEntityWithoutCollection() {
		withStopWatch("Deleting $NAME_C2") {
			Country2.withTransaction() { status ->
				// ignoring the transaction status here, rollback on exception
				Country2.findAll().each { country -> country.delete(flush:true, failOnError:true) }
			}
		}
		assertTrue(Country2.count() == 0)
		assertTrue(Citizen2.count() == 0)
	}



	def withStopWatch(String title, StopWatch stopWatch = stopWatch, Closure c) {
		stopWatch.start(title)
		try {
			c.call()
		} finally {
			stopWatch.stop()
		}

	}
}
