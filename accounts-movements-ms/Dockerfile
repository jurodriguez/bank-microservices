FROM eclipse-temurin:21-jre
WORKDIR /app
COPY deployment/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
