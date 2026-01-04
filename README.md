# examenII-sustitucion

# Documentación de API
# API CRUD de Productos (Node.js)

API con envelope estándar en todas las respuestas:
```json
{
  "data": [],
  "responseCode": "INFO_FOUND",
  "message": "Action executed sucessfully."
}
```

## Response Codes
- `_INFO_FOUND`
- `_INFO_NOT_FOUND`
- `_MISSING_PARAMETERS`
- `_SUCESSFUL` (sic)
- `_DUPLICATE_DATA`
- `_INTERNAL_ERROR`

## Base URL
- PC: `http://localhost:3000`
- Android Emulator: `http://10.0.2.2:3000/`

## Modelo `Producto`
```json
{
  "id": 1,
  "nombre": "string",
  "categoria": "string",
  "precio": 0,
  "cantidad": 0,
  "fotoPath": "/uploads/foto_1736000000000.png"
}
```

---

## Endpoints

### 1) **GET** `/api/products`
Lista todos los productos.

**Request**: sin body.

**Response 200 OK** (con datos)
```json
{
  "data": [
    {
      "id": 1,
      "nombre": "Mouse Inalámbrico",
      "categoria": "Electrónica",
      "precio": 18.99,
      "cantidad": 30,
      "fotoPath": ""
    }
  ],
  "responseCode": "INFO_FOUND",
  "message": "Action executed sucessfully."
}
```

**Response 200 OK** (sin datos)
```json
{
  "data": [],
  "responseCode": "INFO_NOT_FOUND",
  "message": "No data found."
}
```

---

### 2) **GET** `/api/products/{id}`
Obtiene un producto por su ID.

**Response 200 OK**
```json
{
  "data": {
    "id": 1,
    "nombre": "Mouse Inalámbrico",
    "categoria": "Electrónica",
    "precio": 18.99,
    "cantidad": 30,
    "fotoPath": ""
  },
  "responseCode": "INFO_FOUND",
  "message": "Action executed sucessfully."
}
```

**Response 200 OK** (no encontrado)
```json
{
  "data": null,
  "responseCode": "INFO_NOT_FOUND",
  "message": "Product not found."
}
```

---

### 3) **POST** `/api/products`
Crea un producto.

**Request (JSON)**
```json
{
  "nombre": "Cámara DSLR",
  "categoria": "Electrónica",
  "precio": 450.0,
  "cantidad": 5,
  "fotoBase64": "data:image/png;base64,iVBORw0KGgoAAAANSUh..." // opcional
}
```

**Response 201 Created**
```json
{
  "data": {
    "id": 3,
    "nombre": "Cámara DSLR",
    "categoria": "Electrónica",
    "precio": 450.0,
    "cantidad": 5,
    "fotoPath": "/uploads/foto_1736000000000.png"
  },
  "responseCode": "SUCESSFUL",
  "message": "Action executed sucessfully."
}
```

**Response 400 Bad Request**
```json
{
  "data": null,
  "responseCode": "MISSING_PARAMETERS",
  "message": "Nombre y categoría son obligatorios"
}
```

**Response 409 Conflict**
```json
{
  "data": null,
  "responseCode": "DUPLICATE_DATA",
  "message": "Producto duplicado"
}
```

---

### 4) **PUT** `/api/products/{id}`
Actualiza campos de un producto.

**Request (JSON)**
```json
{
  "nombre": "Cámara Mirrorless",
  "categoria": "Electrónica",
  "precio": 399.99,
  "cantidad": 7,
  "fotoBase64": "data:image/jpeg;base64,/9j/4AAQ..." // opcional
}
```

**Response 200 OK**
```json
{
  "data": {
    "id": 3,
    "nombre": "Cámara Mirrorless",
    "categoria": "Electrónica",
    "precio": 399.99,
    "cantidad": 7,
    "fotoPath": "/uploads/foto_1736000001111.png"
  },
  "responseCode": "SUCESSFUL",
  "message": "Action executed sucessfully."
}
```

**Response 404 Not Found**
```json
{
  "data": null,
  "responseCode": "INFO_NOT_FOUND",
  "message": "Product not found."
}
```

---

### 5) **DELETE** `/api/products/{id}`
Elimina un producto.

**Response 200 OK**
```json
{
  "data": { "id": 3 },
  "responseCode": "SUCESSFUL",
  "message": "Action executed sucessfully."
}
```

**Response 404 Not Found**
```json
{
  "data": null,
  "responseCode": "INFO_NOT_FOUND",
  "message": "Product not found."
}
```

### 6) **GET** `/api/categories`
Lista todas las categorías.

**Request**: sin body.

**Response 200 OK** (con datos)
```json
{
    "data": [
        {
            "categoria": "Electrónica"
        },
        {
            "categoria": "Hogar"
        },
        {
            "categoria": "Ferretería"
        },
        {
            "categoria": "Cosmético"
        },
        {
            "categoria": "Oficina"
        },
        {
            "categoria": "Limpieza"
        },
        {
            "categoria": "Otros"
        }
    ],
    "responseCode": "INFO_FOUND",
    "message": "Action executed sucessfully."
}
```

**Response 200 OK** (sin datos)
```json
{
  "data": [],
  "responseCode": "INFO_NOT_FOUND",
  "message": "No data found."
}
```

---

## Ejemplos `curl`

**Crear con imagen PNG**
```bash
curl -X POST http://localhost:3000/api/products   -H "Content-Type: application/json"   -d '{
    "nombre":"Termo Acero",
    "categoria":"Hogar",
    "precio":22.5,
    "cantidad":10,
    "fotoBase64":"data:image/png;base64,iVBORw0KGgo..."
  }'
```

**Actualizar nombre y precio**
```bash
curl -X PUT http://localhost:3000/api/products/2   -H "Content-Type: application/json"   -d '{ "nombre":"Termo 1.2L", "precio":14.90 }'
```

**Eliminar**
```bash
curl -X DELETE http://localhost:3000/api/products/1
```

---

## Puesta en marcha
```bash
npm install
npm start
# API: http://localhost:3000
```
