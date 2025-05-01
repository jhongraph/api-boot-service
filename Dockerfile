# Usa una imagen base oficial de Java con JDK 21
FROM openjdk:21-jdk-slim
# Instalar dependencias necesarias para chromedriver
RUN apt-get update && apt-get install -y \
    libglib2.0-0 \
    libnss3 \
    libgconf-2-4 \
    libfontconfig1 \
    libxss1 \
    libx11-xcb1 \
    libatk-bridge2.0-0 \
    libgtk-3-0 \
    libxdamage1 \
    libxrandr2 \
    libasound2 \
    libxcb1 \
    libpangocairo-1.0-0 \
    libatspi2.0-0 \
    libcups2 \
    libxcomposite1 \
    libxext6 \
    libxfixes3 \
    libxrender1 \
    libxi6 \
    libxtst6 \
    fonts-liberation \
    libgbm1 \
    xdg-utils \
    wget \
    && rm -rf /var/lib/apt/lists/*
# Instala herramientas necesarias para agregar repositorios seguros
RUN apt-get update && apt-get install -y \
    curl \
    gnupg \
    ca-certificates \
    apt-transport-https

# Crea un directorio de trabajo dentro del contenedor
WORKDIR /app
# Copia el JAR desde tu sistema al contenedor
COPY target/boot-001-0.0.1-SNAPSHOT.jar app.jar

COPY . /app

#instalar chrome
RUN apt install -y /app/utilities/google-chrome-stable_current_amd64.deb

EXPOSE 8080


RUN chmod  +x /app/utilities

RUN ls -l /app

#
# Da permisos de ejecución
RUN chmod +x /app/entryPoint.sh

# Probar que chromedriver arranca
#
RUN apt-get update && apt-get install -y dos2unix
#
RUN dos2unix /app/entryPoint.sh

# Usa el script como punto de entrada
ENTRYPOINT ["/bin/bash", "/app/entryPoint.sh"]
