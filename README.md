# Quarkus JSON Data

## Approaches

* **Standalone execution** — run the application without external dependencies.
* **Externalized migrations** — apply migrations outside the application lifecycle.
* **JSON data** — store entities as JSON objects in the database.

## Tools

Development:

* [Nitrite](https://nitrite.dizitart.com/index.html) — standalone database.
* [PostgreSQL](https://www.postgresql.org/) — production database.
* [Hibernate](https://hibernate.org/orm/) — persistence framework.
* [Flyway](https://documentation.red-gate.com/flyway) — migration tool.

Testing:

* [REST-assured](https://rest-assured.io/) — API testing.
* [JSONUnit](https://github.com/lukas-krecan/JsonUnit) — JSON comparison.
* [Testcontainers](https://testcontainers.com/) — environment provisioning.
