# API de Mesa de Ayuda (Helpdesk) con SLA - Spring Boot + JWT

## 1. Descripción

API REST desarrollada con Spring Boot para gestionar tickets de soporte técnico.

El sistema permite que los usuarios registren incidencias mediante tickets y que el equipo de soporte pueda gestionarlos. Cada ticket tiene una prioridad que determina automáticamente su tiempo máximo de atención mediante un SLA (Service Level Agreement).

La API implementa:

- Registro de usuarios.
- Inicio de sesión.
- Autenticación mediante JWT.
- Access Token y Refresh Token.
- Persistencia de Refresh Token en base de datos.
- Revocación de Refresh Token mediante logout.
- Autorización basada en roles (RBAC).
- Gestión de tickets.
- Cálculo automático del SLA.
- Identificación de tickets vencidos.
- Validaciones de datos.
- Manejo global de excepciones.
- Respuestas HTTP según las situaciones establecidas en la guía.

El proyecto corresponde únicamente a una API REST. No se requiere desarrollo de frontend.

---

## 2. Objetivos

El proyecto busca aplicar los siguientes conceptos:

- Construcción de una API REST con Spring Boot.
- Persistencia de información utilizando Spring Data JPA.
- Autenticación mediante JWT.
- Implementación de Access Token y Refresh Token.
- Renovación del Access Token mediante Refresh Token.
- Revocación de Refresh Token.
- Protección de rutas mediante Spring Security.
- Autorización mediante roles.
- Diferenciación entre rutas públicas y protegidas.
- Aplicación de reglas de negocio en el servidor.
- Cálculo automático del SLA de los tickets.

---

## 3. Tecnologías utilizadas

- Java 17+
- Spring Boot 3.5.3
- Spring Web
- Spring Security
- Spring Data JPA
- JWT - JJWT 0.12.6
- BCrypt
- Lombok
- Bean Validation
- MySQL
- Maven
- Postman para pruebas
- spring-dotenv para variables de entorno

---

## 4.Estrategia de Refresh Token

- Para el proyecto se uso la Opción A:
- que es el Refresh Token persistido en la base de datos. 
- Cada refresh token se almacena asociado a un usuario, 
- junto con la fecha de expiración y el estado de la revocación. 
- Esta estrategia permite controlar el ciclo de la vida del token, 
- revocarlo durante el logoout y rechazar intentos posteriores de renovación. Además, facilita el control de sesiones y cumple con el requisito de invalidación.
---


## 5. Base de datos

El proyecto utiliza MySQL como sistema de persistencia.

La base de datos utilizada es:

```text
helpdesk


