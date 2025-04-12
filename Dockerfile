FROM openjdk:17-alpine

RUN mkdir /deploy

ARG JAR_FILE=build/libs/srr-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} /deploy/product.jar

ENV SPRING_PROFILES_ACTIVE=prod,common

CMD ["java", "-jar", "/deploy/product.jar"]
