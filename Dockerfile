FROM openjdk:17-jdk-slim AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn


COPY src src
RUN ./mvnw package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR demo
COPY --from=build target/*.jar demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]