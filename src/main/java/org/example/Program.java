package org.example;

import io.micronaut.runtime.Micronaut;

public class Program {
    private static final String USAGE = "Usage: ticket-service <instance-id> <port>";

    private static int instanceId;
    private static int port;

    /**
     * Main entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println(USAGE);
            return;
        }

        // Try to get the instance id.
        try {
            instanceId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("service-id: instance-id must be an integer");
            System.out.println(USAGE);
            return;
        }

        // Try to get the port.
        try {
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.err.println("service-id: port must be an integer");
            System.out.println(USAGE);
            return;
        }

        // Run the service.
        Micronaut.run(TicketController.class, "-Dmicronaut.server.port=" + port);
    }

    /**
     * Gets the id of this instance.
     */
    public static int getInstanceId() {
        return instanceId;
    }
}