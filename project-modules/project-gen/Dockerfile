FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /project/gen
RUN mkdir -p /project/gen/logs

WORKDIR /project/gen

EXPOSE 9202

ADD ./target/project-gen.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
