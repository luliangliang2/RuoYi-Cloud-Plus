FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /project/system
RUN mkdir -p /project/system/logs
RUN mkdir -p /project/system/temp

WORKDIR /project/system

EXPOSE 9201

ADD ./target/project-system.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
