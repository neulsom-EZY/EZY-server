FROM openjdk:11-jre-slim-buster
ARG JAR_FILE=build/libs/EZY-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /var/jenkins_home/app/app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=test","/var/jenkins_home/app/app.jar"]