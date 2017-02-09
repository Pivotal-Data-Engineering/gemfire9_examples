package io.pivotal.kafka;

import java.util.Random;

import io.pivotal.gemfire9.GemFireStreamingClient;


/**
 * This is a kafka emulator that generates a stream of events
 * @author wwilliams
 *
 */
public class SimpleKafkaProducer {

	private static Random r = new Random();
	private GemFireStreamingClient stbEventsClient = new GemFireStreamingClient();
	private static final int HUNDRED_THOUSAND_PER_MINUTE_EVERY_TEN_SECONDS = 100000 / 60 / 10;

	public static void main(String[] args) {
		new SimpleKafkaProducer().process();
	}

	/**
	 * Stream 10 million records from 1 million devices, 10 txns per device and
	 * occasionally more due to intentional duplicate device IDs
	 */
	public void process() {
		int kafkaQueueId = 1;
		long kafkaOffset = 0;
		for (int i = 0; i < 1000000; i++) {
			String deviceString = "Device" + String.format("%04d", r.nextInt(10000));
			long deviceStartTime = System.currentTimeMillis();

			if (i % HUNDRED_THOUSAND_PER_MINUTE_EVERY_TEN_SECONDS == 0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (i % 10000 == 0) {
					System.out.println("Records so far: " + kafkaOffset);
				}
			}

			// this will write times in descending order for maximum stress
			for (int j = 0; j < 10; j++) {
				String key = deviceString + "|" 
						+ String.format("%013d", deviceStartTime) + "|" 
						+ kafkaQueueId
						+ String.format("%013d", kafkaOffset);
				stbEventsClient.save(key, key);
				deviceStartTime--;
				kafkaOffset++;
			}
		}
	}

}
