# ETAPA 1: Construcción (Build)

FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
# Compilamos el proyecto saltando los tests para ahorrar tiempo y RAM en el build
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (Runtime)
# Usamos una imagen de Java ultra ligera basada en Alpine
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Optimizaciones de RAM para Spring Boot en entornos limitados (t2.micro)
# -Xmx256m: Limita el uso máximo de memoria RAM del montón a 256MB
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseSerialGC -Xss512k"

# Copiamos solo el archivo JAR resultante de la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto que configuramos en application.properties
EXPOSE 8080

# Ejecutamos la aplicación con las optimizaciones de memoria
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]