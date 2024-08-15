FROM openjdk:17

WORKDIR /opt/app

COPY ./target/spring-security-0.0.1-SNAPSHOT.jar .

EXPOSE 8081

CMD [ "java", "-jar", "/opt/app/spring-security-0.0.1-SNAPSHOT.jar" ]