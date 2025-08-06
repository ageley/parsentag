FROM amazoncorretto:24.0.2-alpine3.22
COPY build/libs/parsentag.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
