package io.pivotal.gemfire9;

import java.util.HashMap;
import java.util.Map;

//import com.cc.stbevents.domain.StbEventKey;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;

public class GemFireStreamingClient {

	private static final String EVENTS_REGION = "Events";
	private int recordCtr = 1;
	private int numberOfEntriesPerBatch = 1000;

	private Map<String, String> eventEntries = null;
	private Region<String, String> stbEventsRegion;

	public GemFireStreamingClient() {
		initializeCache();
		eventEntries = new HashMap<>(numberOfEntriesPerBatch);
	}

	public void save(String key, String stbEvent) {
		if (recordCtr % numberOfEntriesPerBatch == 0) {
			stbEventsRegion.putAll(eventEntries);
			recordCtr = 0;
			eventEntries.clear();
		}
		eventEntries.put(key, stbEvent);
		recordCtr++;

	}

	private void initializeCache() {
		ClientCacheFactory ccf = new org.apache.geode.cache.client.ClientCacheFactory();
		ccf.set("cache-xml-file", "./target/classes/config/gemfire/clientCache.xml");
		ClientCache clientCache = ccf.create();
		stbEventsRegion = clientCache.getRegion(EVENTS_REGION);
	}
}
