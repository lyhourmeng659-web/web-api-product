# Web API Product

A RESTful Product Management API built with **Spring Boot**, **Spring Data JPA**, **MySQL**, and **Multipart File Upload** support.

This project allows users to:

- Create products with image upload
- Retrieve all products
- Retrieve a product by ID
- Update product information and image
- Delete products and their uploaded images

---

# Technologies Used

- Java 21 (or your version)
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Lombok
- Multipart File Upload
- Swagger/OpenAPI

---

# Project Structure

```text
src
└── main
    ├── java
    │   └── com.setec
    │       ├── controller
    │       │   └── MyController.java
    │       ├── entities
    │       │   ├── Product.java
    │       │   ├── PostProductDAO.java
    │       │   └── PutProductDAO.java
    │       ├── repos
    │       │   └── ProductRepo.java
    │       ├── config
    │       └── WebApiProductApplication.java
    │
    └── resources

myApp
└── static
    └── Uploaded Images
```

---

# Product Entity

## Fields

| Field | Type | Description |
|---------|---------|---------|
| id | Integer | Product ID |
| name | String | Product Name |
| price | Double | Product Price |
| qty | Integer | Product Quantity |
| imageUrl | String | Image Path |

## Computed Fields

### Amount

```java
amount = price * qty
```

### Full Image URL

```java
http://localhost:8080/static/image.jpg
```

Generated dynamically using:

```java
getFullImageUrl()
```

---

# API Endpoints

Base URL:

```http
http://localhost:8080/api/product
```

---

## 1. Get All Products

### Request

```http
GET /api/product
```

### Success Response

```json
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 800,
    "qty": 2,
    "amount": 1600,
    "fullImageUrl": "http://localhost:8080/static/file.jpg"
  }
]
```

### Empty Response

```json
{
  "message": "Product is Empty!"
}
```

Status:

```http
404 Not Found
```

---

## 2. Get Product By ID

### Request

```http
GET /api/product/1
```

or

```http
GET /api/product/id/1
```

### Success Response

```json
{
  "id": 1,
  "name": "Laptop",
  "price": 800,
  "qty": 2
}
```

### Not Found

```json
{
  "message": "Product ID = 1 not found!"
}
```

---

## 3. Create Product

### Request

```http
POST /api/product
```

Content-Type:

```http
multipart/form-data
```

### Form Data

| Key | Type |
|-------|-------|
| name | Text |
| price | Number |
| qty | Number |
| file | File |

### Example

```text
name = iPhone 15
price = 1200
qty = 10
file = image.jpg
```

### Success Response

```json
{
  "id": 1,
  "name": "iPhone 15",
  "price": 1200,
  "qty": 10
}
```

Status:

```http
201 Created
```

---

## 4. Update Product

### Request

```http
PUT /api/product
```

Content-Type:

```http
multipart/form-data
```

### Form Data

| Key | Type |
|-------|-------|
| id | Number |
| name | Text |
| price | Number |
| qty | Number |
| file | File (Optional) |

### Success Response

```json
{
  "id": 1,
  "name": "Updated Product",
  "price": 1500,
  "qty": 20
}
```

Status:

```http
202 Accepted
```

### Not Found

```json
{
  "message": "Product ID = 1 not found!"
}
```

---

## 5. Delete Product

### Request

```http
DELETE /api/product/1
```

or

```http
DELETE /api/product/id/1
```

### Success Response

```json
{
  "message": "Product ID = 1 has been deleted!"
}
```

Status:

```http
202 Accepted
```

### Not Found

```json
{
  "message": "Product ID = 1 not found!"
}
```

---

# Image Upload

Uploaded images are stored inside:

```text
myApp/static/
```

Example:

```text
myApp/static/8c7e7f53-44fa-image.jpg
```

To avoid filename conflicts, a UUID is automatically generated:

```java
UUID.randomUUID()
```

---

# Database Configuration

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/web_api_product
spring.datasource.username=root
spring.datasource.password=1234

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

# Running the Project

## Clone Repository

```bash
git clone https://github.com/yourusername/web-api-product.git
```

## Navigate Project

```bash
cd web-api-product
```

## Build Project

```bash
./gradlew build
```

Windows:

```bash
gradlew.bat build
```

## Run Project

```bash
./gradlew bootRun
```

or

```bash
java -jar build/libs/web-api-product.jar
```

---

# Swagger Documentation

After running the application:

```text
http://localhost:8080/swagger-ui/index.html
```

Example endpoint documentation:

```text
http://localhost:8080/swagger-ui/index.html#/my-controller
```

---

# Features

✅ RESTful API

✅ CRUD Operations

✅ MySQL Database Integration

✅ Spring Data JPA

✅ File Upload Support

✅ Automatic Image Naming

✅ Dynamic Image URL Generation

✅ Swagger API Documentation

✅ Lombok Integration

---
