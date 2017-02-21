package io.pivotal.redis.gemfire.example.repository;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import example.springdata.redis.test.util.EmbeddedRedisServer;
//import example.springdata.redis.test.util.RequiresRedisServer;
import io.pivotal.redis.gemfire.example.ApplicationConfiguration;

/**
 * @author Gregory Green
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationConfiguration.class)
public class CompanyRepositoryTest<K, V> {

	/**
	 * We need to have a Redis server instance available. <br />
	 * 1) Start/Stop an embedded instance or reuse an already running local installation <br />
	 * 2) Ignore tests if startup failed and no server running locally.
	 */
//	public static @ClassRule RuleChain rules = RuleChain
//			.outerRule(EmbeddedRedisServer.runningAt(6379).suppressExceptions())
//			.around(RequiresRedisServer.onLocalhost().atLeast("3.2"));

	/** {@link Charset} for String conversion **/
	private static final Charset CHARSET = Charset.forName("UTF-8");

	@Autowired RedisOperations<K, V> operations;
	@Autowired CompanyRepository repository;

	/*
	 * Set of test users
	 */
	Address address = new Address("address1", "address2", "cityTown", "stateProvince", "zip", "country");
	
	Company nylaInc = new Company("id:1", "nylaInc" , "info@nylaInc.io", "nylaInc.io", address);
	
	Company logosSquad = new Company("id:2", "logosSquad" , "info@logosSquad.io", "logosSquad.io", address);
	Company shebaNubian = new Company("id:3", "shebaNubian" , "info@shebaNubian.io", "shebaNubian.io", address);
	Company earthMighty = new Company("id:4", "earthMighty" , "info@earthMighty.io", "earthMighty.io", address);
	

	@Before
	@After
	public void setUp() {

		operations.execute((RedisConnection connection) -> {
			connection.flushDb();
			return "OK";
		});
	}

	/**
	 * Save a single entity and verify that a key for the given keyspace/prefix exists. <br />
	 * Print out the hash structure within Redis.
	 */
	@Test
	public void saveSingleEntity() {

		repository.save(nylaInc);

		assertThat(operations.execute(
				(RedisConnection connection) -> connection.exists(new String("companies:" + nylaInc.getId()).getBytes(CHARSET))),
				is(true));
	}

	/**
	 * Find entity by a single {@link Indexed} property value.
	 */
	@Test
	public void findBySingleProperty() {

		flushTestUsers();

		Company company = repository.findOne(nylaInc.getId());
	
		assertEquals(nylaInc, company);
	}

	/**
	 * Find entities by multiple {@link Indexed} properties using {@literal AND}.
	 */
//	@Test
//	public void findByMultipleProperties() {
//
//		flushTestUsers();
//
//		List<Company> companies = repository.findByNameAndEmail(earthMighty.getName(), earthMighty.getEmail());
//
//		assertThat(companies, hasItem(earthMighty));
//		assertThat(companies, not(hasItems(this.nylaInc,this.logosSquad,this.shebaNubian)));
//	}

	/**
	 * Find entities by multiple {@link Indexed} properties using {@literal OR}.
	 */
//	@Test
//	public void findByMultiplePropertiesUsingOr() {
//
//		flushTestUsers();
//
//		List<Company> companies = repository.findByEmailOrTaxID(shebaNubian.getEmail(), shebaNubian.getTaxID());
//
//		assertThat(companies, containsInAnyOrder(shebaNubian));
//		assertThat(companies, not(hasItems(this.nylaInc,this.logosSquad,this.earthMighty)));
//	}

	/**
	 * Find entities in range defined by {@link Pageable}.
	 */
//	@Test
//	public void findByReturingPage() {
//
//		flushTestUsers();
//
//		Page<Company> page1 = repository.findByEmail(nylaInc.getEmail(),  new PageRequest(1, 1));
//
//		assertThat(page1.getNumberOfElements(), is(1));
//		assertThat(page1.getTotalElements(), is(1));
//	}

	/**
	 * Find entity by a single {@link Indexed} property on an embedded entity.
	 */
	@Test
	public void findByEmbeddedProperty() {

		Address winterfell = new Address();
		winterfell.setCountry("the north");
		winterfell.setCityTown("winterfell");

		earthMighty.setAddress(winterfell);

		flushTestUsers();

		List<Company> companies = repository.findByAddress_CityTown(winterfell.getCityTown());

		assertThat(companies, hasItem(this.earthMighty));
		assertThat(companies, not(hasItems(nylaInc,logosSquad,shebaNubian)));
	}

	/**
	 * Find entity by a {@link GeoIndexed} property on an embedded entity.
	 */
//	@Test
//	public void findByGeoLocationProperty() {
//
//		Address winterfell = new Address();
//		winterfell.setCountry("the north");
//		winterfell.setCityTown("winterfell");
//		//winterfell.setLocation(new Point(52.9541053, -1.2401016));
//
//		nylaInc.setAddress(winterfell);
//
//		Address casterlystein = new Address();
//		casterlystein.setCountry("Westerland");
//		casterlystein.setCityTown("Casterlystein");
//		//casterlystein.setLocation(new Point(51.5287352, -0.3817819));
//
//		logosSquad.setAddress(casterlystein);
//
//		flushTestUsers();
//
//		Circle innerCircle = new Circle(new Point(51.8911912, -0.4979756), new Distance(50, Metrics.KILOMETERS));
//		List<Company> companies = repository.findByAddress_LocationWithin(innerCircle);
//
//		assertThat(companies, hasItem(nylaInc));
//		assertThat(companies, hasSize(1));
//
//		Circle biggerCircle = new Circle(new Point(51.8911912, -0.4979756), new Distance(200, Metrics.KILOMETERS));
//		List<Company> nylaAndLogos = repository.findByAddress_LocationWithin(biggerCircle);
//
//		assertThat(nylaAndLogos, hasItems(nylaInc, logosSquad));
//		assertThat(nylaAndLogos, hasSize(2));
//	}

	/**
	 * Store references to other entites without embedding all data. <br />
	 * Print out the hash structure within Redis.
	 */
//	@Test
//	TODO: public void useReferencesToStoreDataToOtherObjects() {
//
//		flushTestUsers();
//
//		eddard.setChildren(Arrays.asList(jon, robb, sansa, arya, bran, rickon));
//
//		repository.save(eddard);
//
//		Person laoded = repository.findOne(eddard.getId());
//		assertThat(laoded.getChildren(), hasItems(jon, robb, sansa, arya, bran, rickon));
//
//		/*
//		 * Deceased:
//		 *
//		 * - Robb was killed by Roose Bolton during the Red Wedding.
//		 * - Jon was stabbed by brothers or the Night's Watch.
//		 */
//		repository.delete(Arrays.asList(robb, jon));
//
//		laoded = repository.findOne(eddard.getId());
//		assertThat(laoded.getChildren(), hasItems(sansa, arya, bran, rickon));
//		assertThat(laoded.getChildren(), not(hasItems(robb, jon)));
//	}

	private void flushTestUsers() {
		repository.save(Arrays.asList(this.nylaInc, this.logosSquad, this.shebaNubian, this.earthMighty));
		//repository.save(this.nylaInc);
	}
}
