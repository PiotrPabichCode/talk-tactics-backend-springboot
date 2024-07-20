FROM gradle:8.2-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle clean build --no-daemon

FROM openjdk:21
COPY --from=build /home/gradle/project/build/libs/talk-tactics-0.0.1-SNAPSHOT.jar app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "/app.jar"]