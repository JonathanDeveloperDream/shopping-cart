# 🚀 Shopping Cart

<p align="center">
  <strong>Sistema de carrito de compras Basado en arquitectura de microservicios.</strong>
</p>

<p align="center">
  El presente proyecto tiene como objetivo poder realizar ordenes pagadas permitiendo consultar las mismas e interactuual con el estado de estas.
</p>

---

# 📚 Índice

* [📌 Variables de Entorno](#-variables-de-entorno)
* [🗄️ Conexión a Base de Datos](#️-conexión-a-base-de-datos)
* [🔄 Flujo Principal](#-flujo-principal)
* [🌐 Endpoints del Flujo Principal](#-endpoints-del-flujo-principal)
* [📖 Documentación Swagger](#-documentación-swagger)
* [🛠️ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
* [👨‍💻 Autor](#-autor)

---

# 📌 Variables de Entorno

Esta sección contiene las variables necesarias para ejecutar correctamente el proyecto.


```env
MYSQL_USER=nombre_de_uduario_de_mysql
MYSQL_PASSWORD=contrasenia del usuario
DB_URL=url_de_conexion_a_mysql

es necesario ultilizar las siguientes url sin hembargo pudes modificarlas segun nececites pero si es critico no modificar los nombre de las db para garantizar su
ejecucion exitosa

customer-service=jdbc:mysql://localhost:3310/customerdb?useSSL=true&serverTimezone=UTC&createDatabaseIfNotExist=true
order-service=jdbc:mysql://localhost:3310/orderdb?useSSL=true&serverTimezone=UTC&createDatabaseIfNotExist=true
order-detail-service=jdbc:mysql://localhost:3310/order_detail_db?useSSL=true&serverTimezone=UTC&createDatabaseIfNotExist=true
payment-service=jdbc:mysql://localhost:3310/payments_db?useSSL=true&serverTimezone=UTC&createDatabaseIfNotExist=true
api-gateway=jdbc:mysql://localhost:3310/securitydb?useSSL=true&serverTimezone=UTC&createDatabaseIfNotExist=true


```

---


---

# 🗄️ Conexión a Base de Datos

## ✅ Ejemplo de estructura YML 

```YML
spring:
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update
    show-sql: 'true'

  application:
    name: customer-service

  servlet:
    multipart:
      max-file-size: 20MB
      max-request-size: 30MB

  datasource:
    password: ${MYSQL_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: ${MYSQL_USER}
    url: ${DB_URL}

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

server:
  servlet:
    context-path: /api/v1/customer
  port: '5000'

springdoc:
  api-docs:
    path: /v3/api-docs

  swagger-ui:
    path: /swagger-ui.html

  default-produces-media-type: application/json


```

---


---

# 🔄 Flujo Principal

El primer Paso seria verificar que tenemos nuestro JDK configurado en nuestro equipo el proyecto utiliza java 21 por lo cual se recomienda usar esta version
Una vez que tenga tu JKD listo debes configurar tus variables de entorno como se ecplica el secciones anteriores.

Luego mi recomendacion es empezar a ejecutar los proyectos en este orden yo lo hice en diferentes ventanas con el IDE Intellij :
eureka-server->customer-service->order-service->order-detail-service->payment-service->api-gateway

una vez ejecutado deberas asegurarte de crear un usuario podras encontrar el endpoitn en el folder de auth de la colleccion de postman que agregue al repositorio
Luego de que hayas podido agregar un usuario deberias agregar los roles a utilizar que seria el Rol de CUSTOMER esto debes hacerlo directo a la base de datos

de esta manera: INSERT INTO `securitydb`.`roles` (`id`, `name`) VALUES ('1', 'CUSTOMER');

luego deberas asignar el role a tu usuario de esta maner:INSERT INTO `securitydb`.`user_roles` (`user_id`, `role_id`) VALUES ('1', '2');

Hasta este punto deberias tener lo necesario para poder interactual con el sistema.


## 📌 Descripción General

La descripcion general es la siguiente, un usuario puede ser un cliente que el cual puede generar una orden esta orden sera pago en el momento y estara vinculada a un detalle en 
el detalle de orden es donde van aspectos generales sobre el producto desde donde se puede ver la informacion completa desde la orden el producto el cliente y el pago
pero una orden debera crearse desde el servicio de orden ya que este es el encargado de esta accion en la cual se a unificado toda la informacion para distribuirla en los distintos servicios correcpondientes

---

## 📍 Flujo Paso a Paso

### 1️⃣ Inicio de solicitud

El cliente realiza una petición al sistema.

### 2️⃣ Validación

El backend valida los datos recibidos.

### 3️⃣ Procesamiento

Se ejecuta la lógica de negocio.

### 4️⃣ Persistencia

La información se almacena en la base de datos.

### 5️⃣ Respuesta

El sistema retorna una respuesta al cliente.

---

# 🌐 Endpoints del Flujo Principal

Partiendo del punto de que ya tienes un usuario deberas iniciar secion 
con el usuario y contrasenia encontraras el endpoint con lo necesario en el folder de auth de postman y te retornara esto:

copia el token y utilizalo como autorizacion bearer token en todas tus peticiones

{
    "message": "¡Bienvenido usuario_prueba!",
    "email": "usuario@correo.com",
    "status": "Login successful",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c3VhcmlvX3BydWViYSIsImlzcyI6ImFuZHJvbWVkYXBwZHJlYW1zIiwicm9sZXMiOlsiQ1VTVE9NRVIiXSwiaWF0IjoxNzc5Mjc2NjA0LCJleHAiOjE3NzkzNjMwMDR9.RuhzCfAKps95bhcBUqWd9Q4bLW1g3rVo30XpYHX1VDU"
}

y luego crea un customer guiandote de los nombre de las capetas de postman
 cuando tengas esto podras crear tu orden con el json que te proporcione en la carpeta de orders y luego de esto
 podras consultar en order detail toda la informacion es el archivo: GetOrderDetailsByOrder
 y tambien podras actualizar los estdos de la orden en los endpoints de orden
 dejare tambien un link auxiliar:

https://jonathan-6920481.postman.co/workspace/Jonathan's-Workspace~2c7926cd-22aa-4b73-83ae-76326dcbe27b/collection/45175988-18fbc0f9-de7f-4077-a780-b3dea762b40c?action=share&source=copy-link&creator=45175988
 
---

# 📖 Documentación Swagger

La documentación interactiva permite probar todos los endpoints del sistema.

## 🌐 Swagger UI

```bash
http://localhost:8080/swagger-ui/index.html
```

---

## 📄 OpenAPI Docs

```bash
http://localhost:8080/v3/api-docs
```

---

## 📌 Dependencia Swagger para Spring Boot

### Maven

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

---

# 🛠️ Tecnologías Utilizadas

| Tecnología         | Uso                               |
| ------------------ | --------------------------------- |
| Java               | Lenguaje principal                |
| Spring Boot        | Backend                           |
| Spring Security    | Seguridad                         |
| Spring Data JPA    | Persistencia                      |
| MySQL              | Base de datos                     |
| mapstruct          | Mapear Dto                        |
| OpenFeign          | Comunicación entre microservicios |
| Resilience4j       | Tolerancia a fallos               |
| Swagger/OpenAPI    | Documentación                     |

---

# 👨‍💻 Autor

## Información

* Nombre: Jonathan Manuel Marin Cruz


---

# ⭐ Notas Finales

* Mantén actualizadas las variables de entorno.
* Nunca subas credenciales reales al repositorio.
* Utiliza perfiles separados para desarrollo y producción.
* Documenta todos los endpoints nuevos.

---
