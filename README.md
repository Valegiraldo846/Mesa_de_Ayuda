# API de Mesa de Ayuda (Helpdesk) con SLA - Spring Boot + JWT

## Descripcion

API REST para gestionar tickets de soporte tecnico con control de SLA (Service Level Agreement), autenticacion JWT con refresh token persistido en base de datos, y autorizacion por roles (RBAC).

## Tecnologias

- Java 17+
- Spring Boot 3.5.3
- Spring Web
- Spring Security
- Spring Data Jpa
- JWT (jjwt 0.12.6)
- BCrypt
- Lombok
- Bean Validation
- MySQL (H2 disponible)
- spring-dotenv para variables de entorno

## Configuracion de la base de datos

El proyecto usa MySQL. Crea una base de datos llamada `helpdesk`:

```sql
CREATE DATABASE helpdesk;
```

Las variables de entorno se cargan desde el archivo `.env` en la raiz del proyecto:

```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=helpdesk
DB_USERNAME=root
DB_PASSWORD=root

JWT_SECRET=TuClaveSecreta
JWT_EXPIRATION=900000
JWT_REFRESH_EXPIRATION=604800000
```

- `JWT_EXPIRATION`: 900000 ms = 15 minutos (duración del access token)
- `JWT_REFRESH_EXPIRATION`: 604800000 ms = 7 días (duración del refresh token)

## Ejecutar el proyecto

```bash
# Con Maven Wrapper
./mvnw spring-boot:run

# O desde IntelliJ: ejecutar la clase MesaDeAyudaApplication
```

## Estrategia de Refresh Token

Se utilizo la **Opcion A - Persistido en base de datos**. Cada refresh token se guarda en la tabla `refresh_token` con su estado (revocado/no revocado) y fecha de expiracion. Esto permite:

- Revocar tokens en logout real
- Invalidar sesiones
- Control del ciclo de vida del token

## Estructura del proyecto

```
com.sena.database_connection.mesadeayuda/
├── config/
│   └── SecurityConfig          # Configuracion de Spring Security
├── controller/
│   ├── AuthController          # Registro, login, refresh, logout
│   ├── TicketController        # CRUD de tickets
│   ├── AdminController         # Ascenso de usuarios
│   └── PingController          # Health check
├── dtos/
│   ├── RegistroRequest         # DTO de registro
│   ├── LoginRequest            # DTO de login
│   ├── LoginResponse           # Respuesta con tokens
│   ├── RefreshTokenRequest     # DTO para refresh
│   ├── TicketDto               # DTO para crear ticket
│   ├── TicketResponseDto       # DTO de respuesta de ticket
│   ├── CambiarEstadoRequest    # DTO para cambiar estado
│   └── AscenderRequest         # DTO para ascender usuario
├── entities/
│   ├── Usuario                 # Entidad de usuario
│   ├── Ticket                  # Entidad de ticket
│   └── RefreshToken            # Entidad de refresh token
├── enums/
│   ├── Rol                     # USUARIO, SOPORTE, ADMIN
│   ├── Prioridad               # BAJA, MEDIA, ALTA
│   └── Estado                  # ABIERTO, EN_PROCESO, RESUELTO
├── exception/
│   ├── GlobalExceptionHandler  # Manejo global de errores
│   ├── RecursoNoEncontradoException
│   ├── EmailYaRegistradoException
│   ├── RefreshTokenInvalidoException
│   ├── CredencialesInvalidasException
│   └── AccesoNoPermitidoException
├── repository/
│   ├── UsuarioRepository
│   ├── TicketsRepository
│   └── RefreshTokenRepository
├── security/
│   ├── JwtUtil                 # Generacion y validacion de JWT
│   ├── JwtAuthenticationFilter # Filtro de autenticacion
│   ├── UsuarioPrincipal        # UserDetails para Spring Security
│   └── UserDetailsServiceImpl  # Carga de usuario desde DB
├── service/
│   ├── AuthService             # Registro, login, refresh, logout
│   ├── UsuarioService          # Gestion de usuarios
│   ├── TicketsService          # Logica de tickets y SLA
│   └── RefreshTokenService     # Gestion de refresh tokens
└── MesaDeAyudaApplication      # Clase principal
```

## Endpoints

### Publicos (sin autenticacion)

| Metodo | Ruta                 | Descripcion                     |
|--------|----------------------|---------------------------------|
| POST   | /api/auth/registro   | Registrar nuevo usuario         |
| POST   | /api/auth/login      | Login y obtener tokens          |
| POST   | /api/auth/refresh    | Renovar access token            |
| GET    | /api/ping            | Verificar que la API esta viva  |

### Autenticados (cualquier usuario logueado)

| Metodo | Ruta                    | Descripcion                          |
|--------|-------------------------|--------------------------------------|
| POST   | /api/auth/logout        | Revocar refresh token                |
| POST   | /api/tickets            | Crear ticket                         |
| GET    | /api/tickets/mios       | Listar mis tickets                   |
| GET    | /api/tickets/{id}       | Consultar ticket (suyo o Soporte/Admin) |

### SOPORTE / ADMIN

| Metodo | Ruta                       | Descripcion              |
|--------|----------------------------|--------------------------|
| GET    | /api/tickets               | Listar todos los tickets |
| PATCH  | /api/tickets/{id}/estado   | Cambiar estado           |
| GET    | /api/tickets/vencidos      | Tickets vencidos por SLA |

### Solo ADMIN

| Metodo | Ruta                | Descripcion                |
|--------|---------------------|----------------------------|
| POST   | /api/admin/soporte  | Ascender usuario a SOPORTE |

## Codigo HTTP esperados

| Situacion                                     | Codigo |
|-----------------------------------------------|--------|
| Creacion exitosa                              | 201    |
| Consulta exitosa                              | 200    |
| Datos invalidos                               | 400    |
| Sin token / token invalido / refresh invalido | 401    |
| Rol insuficiente                              | 403    |
| Recurso no encontrado                         | 404    |
| Email ya registrado                           | 409    |

## Como probar la API

### 1. Registrar usuario

```
POST http://localhost:8080/api/auth/registro
Content-Type: application/json

{
    "nombre": "Juan Perez",
    "email": "juan@test.com",
    "password": "123456"
}
```

### 2. Login

```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "email": "juan@test.com",
    "password": "123456"
}
```

Respuesta:
```json
{
    "accessToken": "...",
    "refreshToken": "..."
}
```

### 3. Crear ticket (con accessToken)

```
POST http://localhost:8080/api/tickets
Authorization: Bearer <accessToken>
Content-Type: application/json

{
    "titulo": "Computador no enciende",
    "descripcion": "El equipo no inicia al presionar el boton de encendido",
    "prioridad": "ALTA"
}
```

El SLA se calcula automaticamente:
- ALTA: 4 horas
- MEDIA: 24 horas
- BAJA: 72 horas

### 4. Renovar access token

```
POST http://localhost:8080/api/auth/refresh
Content-Type: application/json

{
    "refreshToken": "<refreshToken>"
}
```

### 5. Logout (revocar refresh token)

```
POST http://localhost:8080/api/auth/logout
Content-Type: application/json

{
    "refreshToken": "<refreshToken>"
}
```

### 6. Intentar refresh con token revocado -> 401

```
POST http://localhost:8080/api/auth/refresh
Content-Type: application/json

{
    "refreshToken": "<refreshToken_revocado>"
}
```

### 7. Acceso sin token -> 401

```
GET http://localhost:8080/api/tickets/mios
```

### 8. USUARIO intentando acceder a ruta SOPORTE -> 403

```
GET http://localhost:8080/api/tickets
Authorization: Bearer <accessToken_usuario>
```

### 9. Ascender usuario a SOPORTE (solo ADMIN)

```
POST http://localhost:8080/api/admin/soporte
Authorization: Bearer <accessToken_admin>
Content-Type: application/json

{
    "email": "juan@test.com"
}
```
