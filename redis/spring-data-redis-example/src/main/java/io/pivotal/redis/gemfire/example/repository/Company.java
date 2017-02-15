package io.pivotal.redis.gemfire.example.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Gregory Green
 *  Company domain name
 */
@Data
@EqualsAndHashCode()
@RedisHash(value="companies")
@NoArgsConstructor
public class Company
{
	private @Id String id;
	
	private @Indexed String name;
	private String contact;
	private String contactTite;
	private String fax;
	private String phone;
	private String email;
	private String website;
	private String taxID;
	private  Address address;
	/**
	 * 
	 * @param taxID the tax ID
	 * @param name the company name
	 * @param email the email 
	 * @param website the website
	 * @param address the address
	 */
	public Company(String taxID, String name, String email, String website, Address address)
	{
		super();
		this.taxID = taxID;
		this.name = name;
		this.email = email;
		this.website = website;
		this.address = address;
	}
	
	
	
}
