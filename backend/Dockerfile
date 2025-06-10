FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean install -DskipTests

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]


