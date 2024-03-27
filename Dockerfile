FROM maven:3.8.2-amazoncorretto-17 AS build
COPY . .
RUN mvn clean package

FROM openjdk:17
COPY --from=build /target/talk-tactics-0.0.1-SNAPSHOT.jar app.jar
COPY . /app
WORKDIR /app
ENTRYPOINT ["java","-jar","/app.jar"]