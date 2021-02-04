package org.example;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Factory for generating unique ids quickly without knowledge of other factories. Inspired by the twitter snowflake ids.
 */
public class IdFactory {
    private static final long SEQUENCE_OFFSET = 0;
    private static final long SEQUENCE_MAX = 0x1FFFL;
    private static final long SEQUENCE_MASK = SEQUENCE_MAX;
    private static final long SEQUENCE_SIZE_BITS = 12;

    private static final long INSTANCE_ID_OFFSET = SEQUENCE_SIZE_BITS;
    private static final long INSTANCE_ID_MAX = 0x3FFL;
    private static final long INSTANCE_ID_MASK = INSTANCE_ID_MAX << INSTANCE_ID_OFFSET;
    private static final long INSTANCE_ID_SIZE_BITS = 10;

    private static final long TIMESTAMP_OFFSET = INSTANCE_ID_OFFSET + INSTANCE_ID_SIZE_BITS;
    private static final long TIMESTAMP_MAX = 0x1FFFFFFFFFFL;
    private static final long TIMESTAMP_MASK = TIMESTAMP_MAX << TIMESTAMP_OFFSET;
    private static final long TIMESTAMP_SIZE_BITS = 41;

    /**
     * Time in milliseconds for the when the timestamp starts (2021-01-01).
     */
    private static final long EPOCH = 1609459200000L;

    private final int instanceId;

    private volatile long counter = 0;
    private volatile long lastTimestamp = 0;

    /**
     * Creates an instance an {@link IdFactory}.
     *
     * @param instanceId Unique instance id.
     */
    public IdFactory(int instanceId) {
        if (instanceId < 0 || instanceId > INSTANCE_ID_MAX) {
            throw new IllegalArgumentException("instanceId(" + instanceId + ") must be in the range of 0 to " + INSTANCE_ID_MAX);
        }
        this.instanceId = instanceId;

        this.counter = new Random().nextInt((int) INSTANCE_ID_MAX);
    }

    /**
     * Generates the next unique ID.
     *
     * @return next unique ID.
     * @throws InterruptedException throws if interrupted while waiting for the next timestamp.
     */
    public synchronized long next() throws InterruptedException {
        long timestamp = getTimestamp();

        // If the counter has exceeded the maximum value, wait one millisecond to generate again.
        if (counter > SEQUENCE_MAX) {
            while (timestamp == lastTimestamp) {
                TimeUnit.MILLISECONDS.sleep(1);
                timestamp = getTimestamp();
            }

            counter = 0;
        }

        final long sequence = counter;
        counter += 1;

        lastTimestamp = timestamp;

        return makeId(timestamp, instanceId, sequence);
    }

    public static int getInstanceIdFromId(long id) {
        return (int) ((id & INSTANCE_ID_MASK) >> INSTANCE_ID_OFFSET);
    }

    private static long getTimestamp() {
        return System.currentTimeMillis() - EPOCH;
    }

    private static long makeId(long timestamp, long instanceId, long sequence) {
        return ((timestamp << TIMESTAMP_OFFSET) & TIMESTAMP_MASK)
                | ((instanceId << INSTANCE_ID_OFFSET) & INSTANCE_ID_MASK)
                | sequence;
    }
}
