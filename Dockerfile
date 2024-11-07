FROM openjdk:17 
COPY *.war app.war
CMD ["java","-jar","/app.war"]