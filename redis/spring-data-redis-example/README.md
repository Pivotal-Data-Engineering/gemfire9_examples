
#Connect to GemFire redis
/devtools/repositories/NoSQL/redis-3.2.7/src$ ./redis-cli -h localhost -p 11211


#Example commands

localhost:11211> HSET server:name "fido"
OK
localhost:11211>  GET server:name
"fido"

localhost:11211> 
----


Current GeodeRedisAdapter implementation is based on https://cwiki.apache.org/confluence/display/GEODE/Geode+Redis+Adapter+Proposal.
We are looking for some feedback on Redis commands and their mapping to geode region.

==========================

Hello Hitesh,

The following is my feedback
 
2. List Type

I propose using a single partition region (ex: __RedisList) for the List commands.

Region<byteArrayWrapper,ArrayList<byteArrayWrapper>> region;
//Note: ByteArrayWrapper is what the current RedisAdapter uses as its data type. It converts strings to bytes using UTF8 encoding 

Example Redis commands

 RPUSH mylist A =>
 
	 Region<ByteArrayWrapper,List<ByteArrayWrapper>> region = getRegion("__RedisList")
	 List list = getOrCreateList(mylist);
	 list.add(A)
     region.put(mylist,list)
		
 
3. Hashes
 
Based on my Spring Data Redis testing for Hash/object support.

HMSET and similar Hash commands are submitted in the following format: HMSET region:key [field value]+
I proposed creating regions with the following format:

Region<ByteArrayWrapper,Map<ByteArrayWrapper,ByteArrayWrapper>> region;

Also see Hashes section at the following URL https://redis.io/topics/data-types

Example Redis command:
HMSET companies:100 _class io.pivotal.redis.gemfire.example.repository.Company id 100 name nylaInc email info@nylaInc.io website nylaInc.io taxID id:1 address.address1 address1 address.address2 address2 address.cityTown cityTown address.stateProvince stateProvince address.zip zip address.country country  =>

	//Pseudo Access code
	Region<ByteArrayWrapper,Map<ByteArrayWrapper,ByteArrayWrapper>> companiesRegion = getRegion("companies")
	companiesRegion.put(100, toMap(fieldValues))
	
//------
// HGETALL region:key

HGETALL companies:100 =>
	
	Region<key,Map<field,value>> companiesRegion = getRegion("companies")
	return companiesRegion.get(100)

//HSET region:key field value

HSET companies:100 email updated@pivotal.io =>

	Region<key,Map<field,value>> companiesRegion = getRegion("companies");
	Map map = companiesRegion.get(100)
	map.set(email,updated@pivotal.io)
	companiesRegion.put(100,map);

FYI - I started to implement this and hope to submit a pull request soon related to GEODE-2469.

4. Set

I propose using a single partition region (ex: __RedisSET) for the SET commands.

Region<byteArrayWrapper,HashSet<byteArrayWrapper>> region;

Example Redis commands

SADD myset "Hello" =>

	Region<ByteArrayWrapper,Set<ByteArrayWrapper>> region = getRegion("__RedisSET");
	Set set = region(myset)
	boolean bool = set.add(Hello)
	if(bool){
	  region.put(myset,set)
	}
	return bool;


SMEMBERS myset "Hello" =>

		Region<ByteArrayWrapper,Set<ByteArrayWrapper>> region = getRegion("_RedisSET");
			Set set = region(myset)
			return set.contains(Hello)s  

   FYI - I started to implement this and hope to submit a pull request soon related to GEODE-2469.

5. SortedSets
  I propose using a single partition region for the SET commands.

Region<byteArrayWrapper,TreeSet<byteArrayWrapper>> region;e

 
7. Default config for geode-region (vote)
  I think the default setting  should be partitioned with persistence and no redundant copies.
   
8. It seems; redis knows type(list, hashes, string ,set ..) of each key...

  I suggested assuming all keys are strings in UTF8 byte encoding
 
9. Transactions:
  I agree to not support transactions
 
10. Redis COMMAND (https://redis.io/commands/command)
  + for implementing the "COMMAND"
