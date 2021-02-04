package org.example;

import javax.inject.Singleton;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Singleton
public class TicketRepository {
    private final IdFactory idFactory;
    private final Set<Long> ids = new ConcurrentSkipListSet<>();

    private final int instanceId;

    public TicketRepository() {
        instanceId = Program.getInstanceId();
        idFactory = new IdFactory(instanceId);
    }

    /**
     * Creates a new ticket.
     *
     * @return the new ticket id.
     * @throws InterruptedException
     */
    public long newTicket() throws InterruptedException {
        long id = idFactory.next();
        ids.add(id);
        return id;
    }

    /**
     * Checks if a ticket id is valid.
     *
     * @param id Ticket id to check.
     * @return True if the id is valid, otherwise returns false.
     */
    public boolean isValid(long id) {
        final int instanceId = IdFactory.getInstanceIdFromId(id);
        if (instanceId != this.instanceId) {
            // TODO: Check a local cache, remote instance and finally persistent database.
        }

        return ids.contains(id);
    }

    /**
     * Retrieves the number of tickets issued.
     *
     * @return Issued ticket count.
     */
    public int count() {
        // TODO: Check a local cache, remote instance and finally persistent database.

        return ids.size();
    }

    /**
     * Invalidates a ticket id.
     *
     * @param id Ticket id to invalidate.
     * @return True if the id was valid, otherwise returns false.
     */
    public boolean invalidate(long id) {
        final int instanceId = IdFactory.getInstanceIdFromId(id);
        if (instanceId != this.instanceId) {
            // TODO: Check a local cache, remote instance and finally persistent database.
        }

        return ids.remove(id);
    }
}
