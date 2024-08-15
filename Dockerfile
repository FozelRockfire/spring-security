FROM openjdk:17

WORKDIR /opt/app

COPY ./target/*.jar app.jar

EXPOSE 8081

CMD [ "java", "-jar", "-Dspring.profiles.active=docker", "/opt/app/app.jar" ]