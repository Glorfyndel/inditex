# Ejercicio INDITEX:

## Objetivo :
 El objectivo es crear una API RESTful para obtener el precio de un producto en una fecha determinada.
 
## Decisiones de diseño :
 - La API tendrá un endpoint 'iniditex/prices' que es el recurso que se trata en este ejercicio y nada más.
 - La tabla 'PRICES' es identica al enunciado lo que lleva a un formato de fechas (date-time) de tipo 'YYYY-MM-DD-HH.MM.SS' que no es compatible en BBDD por lo que será un 'varchar' <-> 'String'. Estas fechas se mappearán en LocalDateTime en la capa de domain.
 - Para inicializar el proyecto y resolver el ejercicio eh utilizado TDD Outside-In empezando por el test de integración global y bajando hasta la capa de infraestructura.
 - Me he percatado de que el ejercicio se puede resolver enteramente con SQL:
```sql
    SELECT *
    FROM prices p
    WHERE p.brand_id = :brandId
      AND p.product_id = :productId
      AND :applicationDate BETWEEN p.start_date AND p.end_date
    ORDER BY p.priority DESC, p.price_list DESC
        LIMIT 1
```
 - Esta query funciona aunque `start_date` y `end_date` sean varchar porqué siguen un modelo lexicográfico `YYYY-MM-DD-HH.mm.ss > YYYY-MM-DD-HH.mm.ss` siempre y cuando se use en `ORDER BY`. Simplifica mucho la lógica de negocio y por tanto el código.
 - En el enunciado no se explica la regla de negocio si se encuentra varios precios con la misma prioridad. He decidido que se devuelva cualquiera de ellos (el primero que encuentre la query).

## Ejecutar el proyecto en local :
 - He usado una base de datos H2 en memoria para facilitar la ejecución del proyecto, por lo tanto en el `application.yml` tengo la configuracion de la BBDD H2 con `liquibase : true`.

 1. Opción 1 : Usando IDE
  - Clonar el proyecto
  - Ejecutar ```mvn clean install``` en la raíz del proyecto
  - Ejecutar ```mvn spring-boot:run``` en la raíz del proyecto
  - La API estará disponible en 
```bash
    curl -X GET "http://localhost:8080/inditex/prices?productId=35455&brandId=1&applicationDate=2020-06-14T10:00:00"
 ```

 2. Opción 2 : Usando Docker
  - Clonar el proyecto
  - Tener instalado maven y docker (rancher desktop, docker compose por ejemplo)
  - Ejecutar ```maven clean install -DskipTests -P docker``` en la raíz del proyecto
  - Verificar que la image existe con 
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
