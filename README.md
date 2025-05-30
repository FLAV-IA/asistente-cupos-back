
# 🧠 Flav-IA - Backend

Este es el backend del sistema **Flav-IA**, una herramienta diseñada exclusivamente para la dirección de la carrera de 
Licenciatura informática de la UNQ, para que sus directivos, entre ellas la directoria Flavia🤭, 
puedan recibir sugerencias de asignaciones de estudiantes a comisiones, de la manera más justa y eficientemente posible, 
en pos de que conserven un poco la salud mental en cada etapa de inscripciones🙏

El LLM utilizada es GPT-3.5 de Open IA, pero con unos cambios de configuración de `spring.ai`, es compatible con otros modelos.
## 🚀 Stack Tecnológico

- Java 17
- Spring Boot 3
- PostgreSQL
- Docker + Docker Compose
- Gradle
- Spring AI (integración con LLMs)

---

## 🛠️ Requisitos

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Git](https://git-scm.com/)
- (Opcional) JDK 17+ y Gradle si querés correrlo sin Docker

---

## ⚙️ Configuración

1. Cloná el repositorio:

   ```bash
   git clone https://github.com/FLAV-IA/asistente-cupos-back.git
   cd asistente-cupos-back
   ```

2. Copiá el archivo de ejemplo de variables de entorno:

   ```bash
   cp .env.example .env
   ```

3. Ajustá las variables en `.env` si es necesario. Este archivo define configuración sensible como credenciales de la base de datos, 
4. puertos y claves API para integración con modelos de lenguaje.

---

## 🐳 Levantar el proyecto con Docker

```bash
./dev.sh
```

Este script va a:

- Crear y levantar la base de datos PostgreSQL
- Copiar las variables al .env dónde se debe colocar credenciales
- Construir la imagen del backend
- Correr la app en `http://localhost:8080`

> **Nota 1:** El contenedor de la base de datos se inicializa con los datos necesarios si se corre en dev y test con los seeders definidos.
> **Nota 2:** El backend en sus ambientes de dev y test, tiene deshabilitado cors y admite cualquier conexión.

---

## 🧪 Endpoints y Testing

Una vez levantado, podés acceder a la API REST en:

```
http://localhost:8080/asistente
```

Si querés correr los tests manualmente desde el entorno local (fuera de Docker):

```bash
./gradlew test
```

---

## 🧩 Estructura del Proyecto

```
src
├── main
│   ├── java/com/edu/asistenteCupos  ← lógica del backend
│   └── resources                     ← configs, templates, cvs, etc.
└── test
    └── java/com/edu/asistenteCupos  ← tests unitarios e integración
```

---

## 🧹 Limpieza

Para bajar los contenedores y borrar los volúmenes:

```bash
docker-compose down -v
```

---


---

## 📈 Observabilidad: Prometheus + Grafana + Micrometer

Este proyecto ya viene preparado para exponer métricas de rendimiento y monitoreo utilizando Micrometer, Prometheus y Grafana.

### 🔧 Configuración

1. Asegurate de tener el archivo `.env` en la raíz con las siguientes variables:

```env
# Perfil de Spring activo
SPRING_PROFILES_ACTIVE=test

# Base de datos
POSTGRES_PORT=5432
POSTGRES_DB=flavia
POSTGRES_USER=flavia
POSTGRES_PASSWORD=flavia

# OpenAI (si usás el LLM)
CHATGPT_APIKEY=sk-...

# Grafana
GRAFANA_ADMIN_PASSWORD=admin
```

2. Asegurate de que el perfil de Spring esté seteado en `test` y que esté habilitada la configuración Prometheus en el archivo `application-test.properties`:

```properties
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
management.prometheus.metrics.export.enabled=true
```

3. Levantá el stack de observabilidad:

```bash
docker compose -f docker-compose.observabilidad.yml --env-file .env up --build
```

Esto va a levantar:

- `asistente-cupos` en `localhost:8080`
- `Prometheus` en `localhost:9090`
- `Grafana` en `localhost:3000` (usuario: `admin`, contraseña: la de tu `.env`)

---

### 📊 Dashboard de ejemplo

Podés importar los JSON de la carpeta /grafana para visualizar los dashboards pre armados.

También podés editar los paneles con expresiones como:

```txt
rate(llm_priorizacion_batch_seconds_sum[1m]) / rate(llm_priorizacion_batch_seconds_count[1m]) * 1000
optaplanner_score_hard
optaplanner_score_soft
pipeline_priorizacion_total
```

---

### ❓ Preguntas frecuentes

- **¿Dónde veo las métricas?** → `http://localhost:8080/actuator/prometheus`
- **¿Por qué no me aparece ninguna métrica?** → Asegurate de que haya tráfico en la app y que el `MeterRegistry` esté funcionando.
- **¿Qué es `process_uptime_seconds`?** → Es una métrica estándar de Micrometer que indica cuánto tiempo lleva activa la aplicación.


## 🧑‍💻 Contribución

¡Pull requests y sugerencias son bienvenidas! Si vas a colaborar, asegurate de seguir la estructura del proyecto y escribir tests para tu código.

### 📦 Dashboard completo de métricas confirmadas

Se incluye un dashboard listo para importar en Grafana que contiene paneles para todas las métricas reales observadas en Prometheus.

#### 🔹 Descarga el archivo:
[dashboard_asistente_cupos_confirmado.json](./dashboard_asistente_cupos_completos.json)

#### 🔹 Paneles incluidos:
- OptaPlanner - Peticiones asignadas
- OptaPlanner - Score Hard
- OptaPlanner - Score Soft
- OptaPlanner - Tiempo total y máximo de resolución
- OptaPlanner - Errores de resolución
- Pipeline - Sugerencia total (máx)
- Pipeline - Traducción total
- LLM - Priorización batch
- LLM - Traducción batch

Importalo desde Grafana → Dashboards → Import → Cargar JSON, seleccionando Prometheus como datasource.