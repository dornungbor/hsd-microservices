FROM openjdk:8
ADD target/hsd-discovery.jar hsd-discovery.jar
EXPOSE 8761
CMD ["java", "-jar", "-Dspring.profiles.active=local-docker", "hsd-discovery.jar"]

