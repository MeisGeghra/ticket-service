package org.example;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class IdFactoryTests {
    @Test
    public void testValidInstanceId() {
        // Check the minimum bounds.
        try {
            new IdFactory(0);
        } catch (IllegalArgumentException ignored) {
            fail("IdFactory should allow values from 0 to 1023");
        }

        // Check the maximum bounds.
        try {
            new IdFactory(1023);
        } catch (IllegalArgumentException ignored) {
            fail("IdFactory should allow values from 0 to 1023");
        }

        // Check a value in the middle.
        try {
            new IdFactory(512);
        } catch (IllegalArgumentException ignored) {
            fail("IdFactory should allow values from 0 to 1023");
        }
    }

    @Test
    public void testInvalidInstanceId() {
        // Check below the minimum bounds.
        try {
            new IdFactory(-1);
            fail("IdFactory should not allow values below 0.");
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new IdFactory(Integer.MIN_VALUE);
            fail("IdFactory should not allow values below 0.");
        } catch (IllegalArgumentException ignored) {
        }

        // Check above the maximum bounds.
        try {
            new IdFactory(1024);
            fail("IdFactory should not allow values above 1023.");
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new IdFactory(Integer.MAX_VALUE);
            fail("IdFactory should not allow values above 10.");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testUniqueIds() {
        final int MAX_IDS = 100000;

        final ExecutorService executor = Executors.newFixedThreadPool(16);
        final CountDownLatch countDownLatch = new CountDownLatch(MAX_IDS);

        final IdFactory idGenerator = new IdFactory(14);

        final Set<Long> ids = new ConcurrentSkipListSet<>();
        for (int i = 0; i < MAX_IDS; i += 1) {
            executor.submit(() -> {
                try {
                    ids.add(idGenerator.next());
                } catch (InterruptedException ignored) {
                }
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {
        }

        assertEquals(MAX_IDS, ids.size(), "IdFactory should be able to generate a large number of unique ids in a multithreaded environment.");
    }
}
