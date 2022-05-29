FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /project/resource
RUN mkdir -p /project/resource/logs
RUN mkdir -p /project/resource/temp

WORKDIR /project/resource

EXPOSE 9300

ADD ./target/project-resource.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
