# Ejercicio INDITEX:

## Objetivo :
 El objectivo es crear una API RESTfull para obtener el precio de un producto en una fecha determinada.
 
## Decisiones de diseño :
 - La API tendrá un único endpoint 'iniditex/prices' al ser el recurso que se trata en este ejercicio.
 - La tabla 'PRICES' es idéntica al enunciado incluyendo las fechas cuyo formato tipo 'YYYY-MM-DD-HH.MM.SS' no es compatible en BBDD con `date-time` por lo que será un 'varchar' <-> 'String'. Estas fechas se mapearán en `LocalDateTime` para la capa de `domain`.
 - Para inicializar el proyecto y resolver el ejercicio he utilizado `TDD Outside-In` empezando por el test de integración global y bajando hasta la capa de infraestructura.
 - Al tratarse de un ejercicio sencillo, he podido realizar toda la lógica de selección del precio usando esta SQL query nativa con un `ORDER BY` y `LIMIT 1`  para mayor rendimiento y escalabilidad del código:

```sql
    SELECT *
    FROM prices p
    WHERE p.brand_id = :brandId
      AND p.product_id = :productId
      AND :applicationDate BETWEEN p.start_date AND p.end_date
    ORDER BY p.priority DESC, p.price_list DESC
        LIMIT 1
```
 - Esta query funciona aunque `start_date` y `end_date` sean varchar porque siguen un orden lexicográfico `YYYY-MM-DD-HH.mm.ss`, lo que lleva a simplificar mucho la lógica de negocio y, por tanto, el código.
 - En el enunciado no se explica la regla de negocio a aplicar si se encuentran varios precios con la misma prioridad. Por tanto, en tal caso, he determinado recuperar la tarifa más reciente (con el `price_list` mayor).

## Ejecutar el proyecto en local :
 - El proyecto usa una base de datos H2 en memoria.
 - Cuando lo ejecutamos, queremos que exista una tabla `PRICES` con datos iniciales para poder probar la API. Por lo que, en el `application.yml` está configurado para aplicar el liquibase con `liquibase : true`.

### 1. Opción 1 : Usando spring boot y la applicación principal
  - Clonar el proyecto
  - Ejecutar ```mvn clean install``` en la raíz del proyecto
  - Ejecutar ```mvn spring-boot:run``` en la raíz del proyecto
  - La API estará disponible en 
```bash
    curl -X GET "http://localhost:8080/inditex/prices?productId=35455&brandId=1&applicationDate=2020-06-14T10:00:00"
 ```
  - Swagger UI estará disponible en
<http://localhost:8080/swagger-ui/index.html>


### 2. Opción 2 : Usando Docker
  - Clonar el proyecto
  - Tener instalado maven y docker (rancher desktop, docker compose, etc)
  - Ejecutar ```maven clean install -DskipTests -P docker``` en la raíz del proyecto usando el profile `docker` para que genere la imagen docker.
  - Verificar que la imagen existe con :
```bash 
    docker image ls | grep 'inditex'
```
  - Debería aparecer algo así :
```bash
    inditex                                             0.0.1-SNAPSHOT         95f06c6985e0   20 seconds ago   551MB
    inditex                                             latest                 95f06c6985e0   20 seconds ago   551MB
```
  - Ejecutar 
```bash 
  docker run -p 8080:8080 inditex:0.0.1-SNAPSHOT
```
  - La API estará disponible en 
```bash
    curl -X GET "http://localhost:8080/inditex/prices?productId=35455&brandId=1&applicationDate=2020-06-14T10:00:00"
```
- Swagger UI estará disponible en
<http://localhost:8080/swagger-ui/index.html>
