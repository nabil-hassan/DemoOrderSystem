# Overview
Demo REST API system, built using Spring &amp; Hibernate. Runs against MySQL and a suitable servlet container, such as Tomcat.

Built via Gradle; has a set of unit and integration tests, to ensure build stability.

Both sets of tests use JUnit 4:

  * The unit tests are run using the Mockito's JUnit test runner
  * The integration tests are run using Spring test's JUnit runner

# Running the tests

The unit tests run as part of a standard Gradle build, but you can run them separately via the 'test' goal

The integration tests run can be run via the 'persistenceTests' and 'serviceTests' goals.

# Running the system

The system is designed to be run against MySQL 8+ and a suitable servlet container.

The database tables are created and dropped automatically on application startup/shutdown, but you will need to setup the correct schema and user/password on MySQL.

An initial set of data is loaded into the database when the application starts up, to facilitate easy system viewing/testing.

## MySQL Configuration

Once you have MySQL 8 installed, you need to setup the expected schema and user via the mysql prompt, as the root installation user:

```
mysql> create schema demo;
mysql> create user sa identified by 'sa';
mysql> grant all privileges on demo.* to sa;
```

## Servlet Container Configuration

It is expected that the servlet container will accept requests on port 8080, and use the base context path 'demo'. 

## Issuing Requests

The system supports a number of GET requests, but create-update requests will need to be done via a suitable client, whether that be CURL, or something more involved, like SOAPUI. I have chosen to use CURL here, for purposes of simplicity.

|URL Pattern        |Method       |Purpose      |Example
|:-------------------|:-------------|:-------------|:-------
|`http://localhost:8080/demo/api/items/`|GET|Summarises all items in the system|`curl http://localhost:8080/demo/api/items/`
|`http://localhost:8080/demo/api/orders/`|GET|Summarises all orders in the system|`curl http://localhost:8080/demo/api/orders/`
|`http://localhost:8080/demo/api/orders/{orderId}`|GET|Gets details for a specific order|`curl http://localhost:8080/demo/api/orders/1`
|`http://localhost:8080/demo/api/customers/`|GET|Summarises all customers in the system|`curl http://localhost:8080/demo/api/customers/`
|`http://localhost:8080/demo/api/customers/{customerId}`|GET|Gets details for a specific customer|`curl http://localhost:8080/demo/api/customers/1`
|`http://localhost:8080/demo/api/customers/{customerId}/orders`|GET|Gets orders for a specific customer|`curl http://localhost:8080/demo/api/customers/1/orders`
|`http://localhost:8080/demo/api/basket/{customerId}`|GET|Gets details for a specific customer's basket|`curl http://localhost:8080/demo/api/basket/1`
|`http://localhost:8080/demo/api/basket/{customerId}/item/{itemId}`|PUT|Adds an item to a customer's basket|`curl -H "Content-Type: application/json" -X PUT http://localhost:8080/demo/api/basket/1/item/1`
|`http://localhost:8080/demo/api/basket/{customerId}/item/{itemId}`|DELETE|Removes an item from a customer's basket|`curl -H "Content-Type: application/json" -X DELETE http://localhost:8080/demo/api/basket/1/item/1`
|`http://localhost:8080/demo/api/basket/{customerId}/checkout`|POST|Checks out a customer's basket to create a new order|`curl -H "Content-Type: application/json" -d '{"addressId":"1", "cardId":"1"}' -X POST http://localhost:8080/demo/api/basket/1/checkout`
