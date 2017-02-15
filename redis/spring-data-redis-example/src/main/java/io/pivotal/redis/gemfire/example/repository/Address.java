/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.redis.gemfire.example.repository;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.index.GeoIndexed;
import org.springframework.data.redis.core.index.Indexed;

/**
 * @author Christoph Strobl
 * @author Mark Paluch
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
class Address {

	/**
	 * 
	 * @param address1 the address 1
	 * @param address2 the address 2
	 * @param cityTown the city/town
	 * @param stateProvince the state/province
	 * @param zip the zip
	 * @param country the country
	 * @param location
	 */
	public Address(String address1, String address2, String cityTown, String stateProvince, String zip, String country)
	{
		super();
		this.address1 = address1;
		this.address2 = address2;
		this.cityTown = cityTown;
		this.stateProvince = stateProvince;
		this.zip = zip;
		this.country = country;
		//this.location = location;
	}
	private String address1;
	private String address2;
	private @Indexed String cityTown;
	private String stateProvince;
	private String zip;
	private String country;
	//private @GeoIndexed Point location;
}
