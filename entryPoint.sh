#!/bin/bash

echo "🔍 Listando archivos dentro de /app:"
ls -l /app

echo "🔍 Listando archivos dentro de /app/ChromeDriver:"
ls -l /app/ChromeDriver


echo "🚀 Iniciando aplicación Java:"
java -jar app.jar

tail -f /dev/null