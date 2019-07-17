FROM openjdk:8
ADD target/hsd-processor.jar hsd-processor.jar
EXPOSE 8082
CMD ["java", "-jar", "-Dspring.profiles.active=local-docker", "hsd-processor.jar"]

