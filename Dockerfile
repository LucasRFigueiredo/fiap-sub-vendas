# Usa a imagem do OpenJDK 21 para rodar a aplicação
FROM openjdk:21-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR gerado pelo Maven para dentro do container
COPY target/venda-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta da aplicação (8081, conforme `application.properties`)
EXPOSE 8081

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
