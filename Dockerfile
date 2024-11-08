FROM openjdk:17 
ARG JAR_FILE=*.war
COPY ${JAR_FILE} app.war
ENTRYPOINT ["java","-jar","/app.war"]