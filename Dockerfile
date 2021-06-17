FROM openjdk:11-slim-buster

RUN mkdir -p /opt
COPY target/savagerow-1.0-SNAPSHOT.jar /opt/savagerow.jar

ENTRYPOINT ["java", "-jar", "/opt/savagerow.jar"]