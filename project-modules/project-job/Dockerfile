FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /project/job
RUN mkdir -p /project/job/logs
RUN mkdir -p /project/job/temp

WORKDIR /project/job

EXPOSE 9203

ADD ./target/project-job.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
