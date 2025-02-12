FROM openjdk:25-slim-bullseye

WORKDIR /app

ARG JAR_FILE=target/my-spring-boot-app.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
