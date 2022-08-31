FROM amazoncorretto:11-alpine-jdk
COPY build/libs/alfatest-0.0.1-SNAPSHOT.jar alfatest.jar
COPY build/resources/main/application.properties application.properties
ENTRYPOINT ["java","-jar","/alfatest.jar"]