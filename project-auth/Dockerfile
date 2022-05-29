FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /project/auth
RUN mkdir -p /project/auth/logs
RUN mkdir -p /project/auth/temp

WORKDIR /project/auth

EXPOSE 9200

ADD ./target/project-auth.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
