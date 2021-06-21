## build react app
FROM node as stage1
RUN mkdir -p /opt
WORKDIR /opt
COPY . .
WORKDIR /opt/src/main/resources/web
RUN npm install && npm build

WORKDIR /opt/src/main/resources
RUN npm install 
RUN npm run build

## build server
FROM maven as stage2
RUN mkdir -p /opt
WORKDIR /opt
COPY . .
COPY --from=stage1 /opt/src/main/resources/web /opt/src/main/resources/
COPY --from=stage1 /opt/src/main/resources/web/build/* /opt/src/main/resources/public/

RUN mvn clean install

## build distribution
FROM openjdk:11-slim-buster
RUN mkdir -p /opt
COPY --from=stage2 /opt/target/savagerow-1.0-SNAPSHOT.jar opt/savagerow.jar
ENTRYPOINT ["java", "-jar", "/opt/savagerow.jar"]
