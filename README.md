# Quarkus JSON Data

Practical application of an iterative approach to evolving the persistence layer. \
Suitable for focusing on contracts and business logic while evolving the data model. \
Avoids overhead from schema management and complex deployments.

## Flow

1. Use [Nitrite](https://nitrite.dizitart.com/) to move fast during early development.
2. Stabilize the data model through iteration.
3. Eventually, shift to [PostgreSQL](https://www.postgresql.org/) for production use.
4. Optionally, continue to use Nitrite for fast and simple testing.

## Usage

Build the application (production mode):

```shell
docker compose build
```

Start the application (production mode):

```shell
docker compose up -d --wait
```

Stop the application (production mode):

```shell
docker compose down -v
```

Run the application (standalone mode):

```shell
quarkus dev '-Dapp.mode=standalone' '-Dquarkus.hibernate-orm.enabled=false'
```

Run API tests:

```shell
docker run --rm -t -v ${PWD}:/workdir jetbrains/intellij-http-client -D explore.rest
```
