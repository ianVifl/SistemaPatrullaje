# Documentación del proyecto sistemaPatrullaje

Checklist (resumen de lo que contiene este documento):

- [x] Resumen del proyecto
- [x] Requisitos
- [x] Configuración principal (application.properties)
- [x] Cómo compilar / ejecutar localmente
- [x] Descripción de Kafka (topics, productor, consumidor)
- [x] Endpoints REST expuestos
- [x] Archivos importantes y su propósito
- [x] CSV y comportamiento programado
- [x] Errores conocidos y recomendaciones

---

## 1) Resumen

`sistemaPatrullaje` es una aplicación Spring Boot que simula un sistema de alertas/analítica de seguridad.
La aplicación produce mensajes (incidentes) desde un productor que lee un CSV (datos.csv) y los envía a Kafka.
Un consumidor escucha el topic `alertas-seguridad` y persiste los incidentes en una base de datos MySQL.
Además existe un servicio de analítica con un endpoint REST para consultar los sectores más peligrosos.

---

## 2) Requisitos

- Java (JDK) compatible con la versión indicada en `pom.xml` (propiedad `java.version`).
- Maven (se incluye `mvnw` para usar el wrapper).
- MySQL (o configurar otra base de datos y actualizar `application.properties`).
- Kafka (broker accesible; por defecto la app apunta a `localhost:9092`).

Opcional:
- Docker / docker-compose para levantar servicios (hay un `compose.yaml`, revisar antes de usar).

---

## 3) Configuración principal

Archivo: `src/main/resources/application.properties`

Principales propiedades (valores por defecto en el repo):

- spring.datasource.url=jdbc:mysql://localhost:3306/patrullaje_inteligente
- spring.datasource.username=root
- spring.datasource.password=123456789
- spring.jpa.hibernate.ddl-auto=update
- server.port=8081
- spring.kafka.bootstrap-servers=localhost:9092
- spring.kafka.consumer.group-id=patrullaje-group

Si usas Docker Compose, ajusta la URL/credenciales de la base de datos según lo definido en `compose.yaml` o modifica `compose.yaml` para que coincida con `application.properties`.

---

## 4) Cómo compilar y ejecutar

Usando el wrapper de Maven incluido (Linux/Mac):

```bash
# Compilar
./mvnw clean package -DskipTests

# Ejecutar la aplicación
./mvnw spring-boot:run
```

O ejecutar el JAR generado:

```bash
java -jar target/sistemaPatrullaje-0.0.1-SNAPSHOT.jar
```

Si quieres levantar servicios con Docker Compose (MySQL, etc.) revisa y corrige `compose.yaml` antes de usar:

```bash
# (Ejemplo) levantar los servicios
docker compose -f compose.yaml up -d
```

Nota: el `compose.yaml` incluido en el repo es básico y sus variables (base de datos, usuarios, contraseñas y puertos) pueden no coincidir con `application.properties`. Ajusta según necesites.

---

## 5) Kafka: productor y consumidor

- Topic usado por el proyecto: `alertas-seguridad`.

- Productor: `KafkaProducerService` (pkg `service`) -> Lee `src/main/resources/datos.csv` línea por línea y, cada 2 segundos (schedule fixedRate = 2000 ms), crea un objeto `Incidente` (con tipo, prioridad, sector y descripción) y lo envía como JSON al topic `alertas-seguridad`.

- Consumidor: `KafkaConsumerService` (pkg `service`) -> Escucha `alertas-seguridad` (groupId `patrullaje-group`) y persiste los objetos `Incidente` usando `IncidenteRepository`.

Comprobaciones recomendadas:
- Asegúrate de que Kafka esté corriendo en `spring.kafka.bootstrap-servers`.
- Verifica que el topic `alertas-seguridad` exista o que el broker permita la auto-creación.

---

## 6) Endpoints REST

API principal disponible (base URL por defecto http://localhost:8081):

- GET /api/v1/analitica/top-sectores
  - Devuelve los top 3 sectores más peligrosos (lista de `SectorPeligrosoDTO`).

Ejemplo con curl:

```bash
curl http://localhost:8081/api/v1/analitica/top-sectores
```

---

## 7) Archivos / clases importantes

- `src/main/java/com/ianvifit/sistemapatrullaje/SistemaPatrullajeApplication.java` — clase principal; habilita `@EnableScheduling`.
- `src/main/resources/datos.csv` — CSV de donde el productor toma ejemplos históricos.
- `KafkaProducerService.java` — productor programado que emite eventos a Kafka.
- `KafkaConsumerService.java` — consumidor que guarda incidentes en la base de datos.
- `AnaliticaService.java` y `AnaliticaController.java` — lógica y endpoint para obtener analytics (top sectores).
- `Incidente`, `Prioridad`, `TipoIncidente` — modelos (entities) usados para persistencia.
- `IncidenteRepository` — interfaz JPA para persistir incidentes.

---

## 8) CSV y comportamiento programado

- El productor abre `datos.csv` al arrancar (método `@PostConstruct`) y salta la primera línea (cabecera).
- Cada 2 segundos (`@Scheduled(fixedRate = 2000)`) lee la siguiente línea y envía un `Incidente` a Kafka. Cuando llega al final del archivo, reinicia la lectura (se invoca `init()` otra vez).

Recomendaciones:
- Asegúrate de que `src/main/resources/datos.csv` exista y esté bien formateado (se asume coma `,` como separador y la columna de interés está en el índice 4).

---

## 9) Errores conocidos y recomendaciones

He detectado algunas inconsistencias que conviene revisar antes de ejecutar en producción:

1) `KafkaConsumerService` importa una clase equivocada:

   - Inexistente/errónea: `import tools.jackson.databind.ObjectMapper;`
   - Correcta: `import com.fasterxml.jackson.databind.ObjectMapper;`

   Si no se corrige, el proyecto no compilará porque esa importación no existe. Reemplaza la importación y recompila.

2) `compose.yaml` y `application.properties` usan credenciales y nombres de base de datos distintos. Si vas a usar Docker Compose, sincroniza credenciales/DB con `application.properties` o modifica este último.

3) `pom.xml` especifica `java.version` como `21.0.11` (revisar compatibilidad con tu JDK). Asegúrate de usar una JDK compatible.

4) La configuración de Kafka por defecto apunta a `localhost:9092`. Si tu broker está en otra máquina o en Docker, actualiza `spring.kafka.bootstrap-servers`.

---

## 10) Cómo contribuir / pruebas rápidas

- Ejecuta pruebas (si las hay):

```bash
./mvnw test
```

- Para desarrollo iterativo, usa `spring-boot:run` y edita el código; la app reiniciará si usas Spring Dev Tools (no incluido por defecto).

---

Si quieres, puedo:

- Añadir un archivo README.md más conciso listo para GitHub.
- Corregir automáticamente la importación en `KafkaConsumerService.java` y otros problemas detectados.
- Crear un `docker-compose` completo que incluya Kafka + Zookeeper + MySQL con credenciales sincronizadas.

Indícame cuál de las opciones prefieres y lo implemento.

