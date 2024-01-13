# Build stage
FROM maven:3.8.6-openjdk-11-slim AS build

WORKDIR /app
COPY . .
RUN mvn clean package

# Setup tomcat
FROM tomcat:10.1.18-jre11-temurin-jammy

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build ./app/target/tradesys-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/tradesys.war
#COPY ./DSOURCE.xml /usr/local/tomcat/conf/Catalina/localhost/DSOURCE.xml
#COPY ./tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
#RUN cp -r /usr/local/tomcat/webapps.dist/manager /usr/local/tomcat/webapps

# Run
EXPOSE 8080
CMD ["catalina.sh", "run"]