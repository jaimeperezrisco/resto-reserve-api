# Guia Postman - API RestoReserve

## 1) Arranque del proyecto
- Ejecuta la app desde IDE o con `mvn spring-boot:run`
- Base URL: `http://localhost:8080`

## 2) Autenticacion

### 2.1 Register (crear usuario)
**POST** `http://localhost:8080/api/v1/auth/register`

Headers:
- `Content-Type: application/json`

Body (raw JSON):
```json
{
  "username": "admin",
  "password": "1234",
  "fullName": "Admin User"
}
```

Respuesta esperada:
```json
{ "token": "..." }
```

### 2.2 Login (obtener token)
**POST** `http://localhost:8080/api/v1/auth/login`

Headers:
- `Content-Type: application/json`

Body (raw JSON):
```json
{
  "username": "admin",
  "password": "1234"
}
```

Respuesta esperada:
```json
{ "token": "..." }
```

## 3) Configurar Bearer Token en Postman

Una vez tengas el token:
- Authorization -> **Bearer Token**
- Pega el token en el campo
- Este token se usa para todos los endpoints protegidos

## 4) Mesas (requiere ADMIN)

### 4.1 Crear mesa
**POST** `http://localhost:8080/api/v1/tables`

Headers:
- `Content-Type: application/json`
- Authorization: Bearer token

Body:
```json
{
  "name": "Mesa 1",
  "capacity": 4
}
```

Respuesta esperada: **201 Created**

### 4.2 Listar mesas
**GET** `http://localhost:8080/api/v1/tables`

Headers:
- Authorization: Bearer token

Respuesta esperada: **200 OK**

## 5) Reservas

### 5.1 Crear reserva
**POST** `http://localhost:8080/api/v1/reservations`

Headers:
- `Content-Type: application/json`
- Authorization: Bearer token

Body:
```json
{
  "tableId": 1,
  "reservationDate": "2026-06-01T20:00:00",
  "numberOfGuests": 2
}
```

Respuesta esperada: **201 Created**

### 5.2 Listar reservas
**GET** `http://localhost:8080/api/v1/reservations`

Headers:
- Authorization: Bearer token

Respuesta esperada: **200 OK**

### 5.3 Cancelar reserva
**DELETE** `http://localhost:8080/api/v1/reservations/1`

Headers:
- Authorization: Bearer token

Respuesta esperada: **204 No Content**

## 6) Pruebas de errores (utiles para la defensa)

### 6.1 400 Bad Request (validaciones DTO)
Ejemplo: enviar `numberOfGuests` con 0 o fecha en el pasado:
```json
{
  "tableId": 1,
  "reservationDate": "2020-01-01T10:00:00",
  "numberOfGuests": 0
}
```

### 6.2 401 Unauthorized
- Quita el token y llama a cualquier endpoint protegido

### 6.3 403 Forbidden
- Intenta acceder a `/api/v1/tables` con un usuario sin rol ADMIN

## 7) Errores comunes

- **Error de fecha**: `reservationDate` es `LocalDateTime`, usa formato completo `YYYY-MM-DDTHH:mm:ss`
- **Error de null en int**: asegúrate de enviar `numberOfGuests` y que no sea `null`
- **403 en /auth**: no enviar Authorization y confirmar que sea POST
