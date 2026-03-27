# Quarkus JSON Data

Practical application of an iterative approach to evolving the persistence layer. \
Suitable for focusing on contracts and business logic while evolving the data model. \
Avoids overhead from schema management and complex deployments.

## Flow

1. Use [Nitrite](https://nitrite.dizitart.com/) to move fast during early development.
2. Stabilize the data model through iteration.
3. Eventually, shift to [PostgreSQL](https://www.postgresql.org/) for production use.
4. Optionally, continue to use Nitrite for fast and simple testing.

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
docker compose down -v
```

Test:

```shell
docker run --rm -t -v ${PWD}:/workdir jetbrains/intellij-http-client -D explore.rest
```

Run (standalone):

```shell
quarkus dev "-Dapp.mode=standalone" "-Dquarkus.hibernate-orm.enabled=false"
```
