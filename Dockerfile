FROM maven:3.9-eclipse-temurin-25 AS build
COPY . ./app
WORKDIR /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre-jammy
EXPOSE 8080
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]