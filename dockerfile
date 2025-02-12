FROM openjdk:25-slim-bullseye

WORKDIR /app

ARG JAR_FILE=target/my-spring-boot-app.jar

COPY target/my-spring-boot-app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
