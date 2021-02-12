# Stage 1
FROM maven:3.6.3-openjdk-11-slim as stage-1
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn clean package

# Stage 2 - setup and run app in the jre environment
FROM openjdk:11-jre-slim-buster
RUN mkdir /app
COPY --from=stage-1 /usr/src/app/target/resource-1.0.0-SNAPSHOT.jar /app/
WORKDIR /app
EXPOSE 9090
CMD java -jar resource-1.0.0-SNAPSHOT.jar

