# Ticket Service
HTTP based service for handling ticket IDs.

## Building
In the root directory run: ```mvn package```

The resulting jar can be found in the `./target` directory with the name `ticket-service-<version>.jar`.

## Running
To run, navigate into the `./target` directory and call the following from the command line. 

```java -jar ticket-service-<version>.jar <instance-id> <port>```.

The `instance-id` should be a unique id in the range of 0 to 1023 provided to this specific instance. 

`port` can be any valid unbound port.

## Usage

#### Public methods:

To generate a new ticket id: `GET http://<hostname>:<port>/ticket/id-new`. Returns a unique 64-bit id stored as a hexadecimal string.

To query if a ticket id is valid: `GET http://<hostname>:<port>/ticket/id-is-valid/{id}`. Returns the text "true" if the ticket {id} is valid otherwise returns "false".

#### Internal methods:

The internal methods requires a token `FAKE_TOKEN` to represent basic authorization.

To query the number of issued tickets: `GET http://<hostname>:<port>/ticket/count?token={token}`. Returns the number of tickets issued as a 64-bit signed integer.

To invalidate a ticket ID: `POST http://<hostname>:<port>/ticket/id-is-valid/{id}?token={token}`. Invalidates the ticket {id}, returns "true" if the id was invalidated, otherwise returns "false".

