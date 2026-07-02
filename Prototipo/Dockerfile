# ETAPA 1: Construcción (usamos maven para compilar)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (usamos tomcat)
FROM tomcat:10.1
# Borrar contenido por defecto de tomcat
RUN rm -rf /usr/local/tomcat/webapps/*
# Copiar el archivo .war generado en la etapa anterior
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/snaar.war