FROM openjdk:8
ADD target/hsd-server.jar hsd-server.jar
EXPOSE 8081
CMD ["java", "-jar", "-Dspring.profiles.active=local-docker", "hsd-server.jar"]

