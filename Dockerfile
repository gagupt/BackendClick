FROM openjdk:8u171-jdk-stretch
ADD build/libs/Sample1-0.0.1-SNAPSHOT.jar Sample1-0.0.1-SNAPSHOT.jar
EXPOSE 8085 
ENTRYPOINT ["java", "-jar", "Sample1-0.0.1-SNAPSHOT.jar"]
