# Label
LABEL maintainer="duongph"
LABEL version="1.0"
LABEL org.opencontainers.image.source=https://github.com/duongphhe151097/SWP391_TradeSystem

# Arguments
ARG MAVEN_VERSION=3.8.6-openjdk-11-slim
ARG TOMCAT_VERSION=10.1.18-jre11-temurin-jammy
ARG TIMEZONE=Asia/Ho_Chi_Minh

# Build stage
FROM maven:${MAVEN_VERSION} AS build
WORKDIR /app

COPY . .
COPY ./deploy_settings/persistence.xml ./src/main/resources/META-INF/persistence.xml
COPY ./deploy_settings/common.js ./src/main/webapp/js
RUN mvn clean package

# Setup tomcat
FROM tomcat:${TOMCAT_VERSION}
ENV TZ=${ARG}

RUN groupadd -r tradesys && useradd -r -g tradesys -m tradesys && \
    chown -R tradesys:tradesys /usr/local/tomcat && \
    rm -rf /usr/local/tomcat/webapps/*
COPY --from=build ./app/target/tradesys-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
USER tradesys

EXPOSE 8080
CMD ["catalina.sh", "run"]