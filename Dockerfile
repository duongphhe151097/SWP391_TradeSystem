# Build stage
FROM maven:3.8.6-openjdk-11-slim AS build
ENV TZ=Asia/Ho_Chi_Minh

WORKDIR /app
COPY . .
COPY ./deploy_settings/persistence.xml /src/main/resources/META-INF/persistence.xml
COPY ./deploy_settings/captcha.js /src/main/webapp/js
RUN mvn clean package

# Setup tomcat
FROM tomcat:10.1.18-jre11-temurin-jammy

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build ./app/target/tradesys-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Run
EXPOSE 8080
CMD ["catalina.sh", "run"]