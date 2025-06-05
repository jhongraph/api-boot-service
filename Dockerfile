# 🛠 Etapa 1: Compilación del proyecto Java con Maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app
COPY . .

# Compila el proyecto sin correr los tests
RUN mvn clean package -DskipTests

# 🧼 Etapa 2: Imagen final minimalista con JDK y Chromium
FROM bellsoft/liberica-openjdk-alpine:21

# Instala Chrome, Chromedriver y dependencias necesarias
RUN apk add --no-cache \
    chromium \
    chromium-chromedriver \
    nss \
    freetype \
    harfbuzz \
    ttf-freefont \
    bash \
    curl \
    dos2unix \
    dumb-init \
    python3 \
    py3-pip \
    zlib

# Instalar Python y Flask
RUN pip3 install flask --break-system-packages

# Descarga e instala Edge WebDriver
ENV EDGE_VERSION=126.0.2578.1
RUN wget -O /tmp/edgedriver.zip https://msedgedriver.azureedge.net/${EDGE_VERSION}/edgedriver_linux64.zip && \
    unzip /tmp/edgedriver.zip -d /usr/bin && \
    chmod +x /usr/bin/msedgedriver


# Define ruta de trabajo
WORKDIR /app

# Copia solo el jar ya construido desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Copia recursos de tu app como entryPoint.sh y otros archivos
COPY . /app

# Variable de entorno para Spring
# ENV driver.path=/usr/bin/chromedriver

ENV driver.path=/usr/bin/msedgedriver
ENV SPRING_CONFIG_LOCATION=/app/utilities/application.yaml

# Permisos
RUN chmod +x /app/entryPoint.sh /app/utilities && dos2unix /app/entryPoint.sh

# Exposición del puerto
EXPOSE 8080

# Punto de entrada
ENTRYPOINT ["/bin/bash", "/app/entryPoint.sh"]
