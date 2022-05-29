FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /project/gateway
RUN mkdir -p /project/gateway/logs
RUN mkdir -p /project/gateway/temp

WORKDIR /project/gateway

EXPOSE 8080

ADD ./target/project-gateway.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
