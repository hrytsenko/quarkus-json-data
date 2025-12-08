FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /build

COPY mvnw pom.xml ./
COPY .mvn ./.mvn
RUN ./mvnw -q -B dependency:go-offline

COPY src ./src
RUN ./mvnw -q -B package

FROM gcr.io/distroless/java21-debian12

WORKDIR /deploy

COPY --from=build /build/target/quarkus-app/lib/ ./lib/
COPY --from=build /build/target/quarkus-app/*.jar ./
COPY --from=build /build/target/quarkus-app/app/ ./app/
COPY --from=build /build/target/quarkus-app/quarkus/ ./quarkus/

EXPOSE 8080
USER nonroot

ENTRYPOINT ["java", "-Dquarkus.http.host=0.0.0.0", "-jar", "quarkus-run.jar"]
