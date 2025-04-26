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
# Agrega la clave GPG para Google Chrome
RUN mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /etc/apt/keyrings/google-linux-signing.gpg
# Agrega el repositorio de Chrome estable
RUN echo "deb [arch=amd64 signed-by=/etc/apt/keyrings/google-linux-signing.gpg] http://dl.google.com/linux/chrome/deb/ stable main" \
    > /etc/apt/sources.list.d/google-chrome.list
# Instala Chrome estable
RUN apt-get update && apt-get install -y google-chrome-stable
# Crea un directorio de trabajo dentro del contenedor
WORKDIR /app
# Copia el JAR desde tu sistema al contenedor
COPY target/boot-001-0.0.1-SNAPSHOT.jar app.jar

COPY . /app

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
