ARG MAVEN_VERSION=3.8.6-openjdk-11-slim
ARG TOMCAT_VERSION=10.1.18-jre11-temurin-jammy

#FROM alpine/git:latest AS git_clone
#WORKDIR /app
#RUN git clone https://github.com/duongphhe151097/SWP391_TradeSystem.git .

# Build stage
FROM maven:${MAVEN_VERSION} AS build
WORKDIR /app
#COPY --from=git_clone /app /app
#COPY --from=git_clone /app/deploy_settings/persistence.xml /app/src/main/resources/META-INF/persistence.xml
#COPY --from=git_clone /app/deploy_settings/common.js /app/src/main/webapp/js

RUN rm -rf /app/Dockerfile
RUN rm -rf /app/deploy_settings
COPY . .
COPY ./deploy_settings/persistence.xml ./src/main/resources/META-INF/persistence.xml
COPY ./deploy_settings/common.js ./src/main/webapp/js
RUN mvn clean package
RUN rm -rf /app/.git

# Setup tomcat
FROM tomcat:${TOMCAT_VERSION}
ENV TZ=Asia/Ho_Chi_Minh
# Add group tradesys
RUN groupadd -r tradesys && useradd -r -g tradesys -m tradesys
# Change dir owner to tradesys
RUN chown -R tradesys:tradesys /usr/local/tomcat

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build ./app/target/tradesys-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

#Change user to tradesys
USER tradesys
# Run
EXPOSE 8080
CMD ["catalina.sh", "run"]