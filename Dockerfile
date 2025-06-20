# Usa una imagen base de OpenJDK para ejecutar la aplicación
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR construido dentro del contenedor
COPY target/auth-0.0.1-SNAPSHOT.jar app.jar

# Expón el puerto 8080 para que sea accesible desde fuera del contenedor
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "/app/app.jar"]