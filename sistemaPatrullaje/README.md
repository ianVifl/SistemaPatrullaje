# sistemaPatrullaje — instrucciones de inicio

Este repositorio contiene una aplicación Spring Boot que produce y consume eventos de seguridad usando Kafka y persiste incidentes en MySQL.

Archivos añadidos para facilitar el inicio:

- `docker-compose.yml` — Levanta Zookeeper, Kafka y MySQL configurados para uso local (puertos 2181, 9092 y 3306).
- `start.sh` — Script para compilar y ejecutar el JAR del proyecto (usa `./mvnw`).
- `DOCUMENTACION.md` — Documentación extendida del proyecto.

Requisitos locales mínimos:

- Java (JDK) compatible con la versión en `pom.xml`.
- Docker y Docker Compose (si quieres levantar Kafka + MySQL con los contenedores).
- Maven (opcional si usas `./mvnw`).

Pasos rápidos para iniciar todo (modo recomendado para desarrollo local):

1) Levanta Kafka y MySQL usando Docker Compose:

```bash
docker compose up -d
```

Esto iniciará Zookeeper, Kafka y MySQL. La base de datos creada por defecto es `patrullaje_inteligente` con contraseña de root `123456789` (coincide con `src/main/resources/application.properties`).

2) Espera a que los servicios estén listos (especialmente Kafka y MySQL). Puedes revisar logs:

```bash
docker compose logs -f kafka
docker compose logs -f mysql
```

3) Compila y ejecuta la app desde la raíz del proyecto:

```bash
chmod +x start.sh
./start.sh
```

Alternativamente, puedes ejecutar con el wrapper de Maven directamente:

```bash
./mvnw spring-boot:run
```

Endpoints útiles:

- GET http://localhost:8081/api/v1/analitica/top-sectores — Devuelve los 3 sectores más peligrosos.

Notas y advertencias:

- `docker-compose.yml` está pensado para desarrollo local. Revisa/ajusta si lo usas en otra red o en CI.
- Si tu broker Kafka no está en `localhost:9092` (por ejemplo lo has levantado en otra máquina o con otra configuración), actualiza `src/main/resources/application.properties` (propiedad `spring.kafka.bootstrap-servers`).
- Hay un problema detectado en el código fuente: `KafkaConsumerService` importa `tools.jackson.databind.ObjectMapper` en lugar de `com.fasterxml.jackson.databind.ObjectMapper`. Recomendación: corregir esa importación antes de compilar.

¿Quieres que corrija la importación errónea en `KafkaConsumerService.java` y haga commit automático? Puedo hacerlo ahora.

