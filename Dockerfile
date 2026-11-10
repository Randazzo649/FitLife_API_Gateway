
# Etapa 1: Compilação
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

# Etapa 2: Execução
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/target/fitlife-api-gateway-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
