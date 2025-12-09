# Quarkus JSON Data

## Approaches

* **Standalone execution** — run or test the application without external dependencies.
* **Externalized migrations** — apply migrations outside the application lifecycle.
* **JSON entities** — store entities as JSON objects.

## Tools

* [Nitrite](https://nitrite.dizitart.com/) — standalone database.
* [PostgreSQL](https://www.postgresql.org/) — production database.
* [Flyway](https://documentation.red-gate.com/flyway/) — migration tool.
* [pgAdmin](https://www.pgadmin.org/) — management tool.
* [PostgREST](https://postgrest.org/) — data gateway.
* [Hibernate](https://hibernate.org/orm/) — persistence framework.

## Commands

Build:

```shell
docker compose build
```

Deploy:

```shell
docker compose up -d --wait
```

Undeploy:

```shell
docker compose down
```

Test:

```shell
docker run --rm -t -v ${PWD}:/workdir jetbrains/intellij-http-client -D explore.rest
```

Run (standalone):

```shell
quarkus dev "-Dapp.mode=standalone" "-Dquarkus.hibernate-orm.enabled=false"
```
