FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /project/monitor
RUN mkdir -p /project/monitor/logs

WORKDIR /project/monitor

EXPOSE 9100

ADD ./target/project-monitor.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
