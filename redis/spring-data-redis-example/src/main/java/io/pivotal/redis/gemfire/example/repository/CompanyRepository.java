package io.pivotal.redis.gemfire.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Circle;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, String> 
{

	List<Company> findByName(String lastname);

	Page<Company> findByContact(String contact, Pageable page);
	
	List<Company> findByNameAndEmail(String name, String email);
	
	List<Company> findByEmailOrTaxID(String email, String taxID);

	List<Company> findByEmail(String email);

	List<Company> findByAddress_CityTown(String cityTown);

	List<Company> findByAddress_LocationWithin(Circle circle);
}
