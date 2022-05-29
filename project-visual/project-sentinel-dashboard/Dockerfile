FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /project/sentinel-dashboard
RUN mkdir -p /project/sentinel-dashboard/logs

WORKDIR /project/sentinel-dashboard

EXPOSE 8718

ADD ./target/project-sentinel-dashboard.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
