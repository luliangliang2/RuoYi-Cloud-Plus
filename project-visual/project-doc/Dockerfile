FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /project/doc
RUN mkdir -p /project/doc/logs

WORKDIR /project/doc

EXPOSE 18000

ADD ./target/project-doc.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
