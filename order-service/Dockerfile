FROM maven:3-jdk-11-slim AS MAVEN_TOOL_CHAIN
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]




