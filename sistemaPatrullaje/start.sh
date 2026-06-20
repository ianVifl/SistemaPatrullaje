#!/usr/bin/env bash
set -euo pipefail

echo "Compilando el proyecto con Maven Wrapper..."
./mvnw clean package -DskipTests

JAR=target/sistemaPatrullaje-0.0.1-SNAPSHOT.jar

if [ ! -f "$JAR" ]; then
  echo "Jar no encontrado en $JAR"
  exit 1
fi

echo "Iniciando la aplicación..."
java -jar "$JAR"

