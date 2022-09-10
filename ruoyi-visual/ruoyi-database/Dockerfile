FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /ruoyi/database
RUN mkdir -p /ruoyi/database/logs

WORKDIR /ruoyi/database

EXPOSE 9205

ADD ./target/ruoyi-database.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
