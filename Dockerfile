FROM openjdk:8-alpine
EXPOSE 8080
WORKDIR /app
COPY . /app
#RUN ./mvnw package -DskipTests
ARG JAR_FILE=tortuga-api-0.0.1.1-SNAPSHOT.jar
ADD target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
