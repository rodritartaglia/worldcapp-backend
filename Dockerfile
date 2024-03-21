FROM amazoncorretto:17.0.5
EXPOSE 9000
COPY build/libs/proyecto-base-tp-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]