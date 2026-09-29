<h1 align="center">💳 Payment Gateway Engine 🚀</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Microservice-Payment%20Engine-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Status-Active-success?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Backend-SpringBoot-green?style=for-the-badge" />
</p>

<p align="center">
  <em>
    High-throughput <b>Payment Processing System</b> built using <b>Spring Boot, Kafka, Redis, PostgreSQL</b><br>
    Designed for <b>Scalability ⚡ | Reliability 🔐 | Performance 🚀</b>
  </em>
</p>

---

# 🧠 System Architecture

```
Frontend → Spring Boot → PostgreSQL → Kafka → Consumer → Redis → Zipkin
```

---

# 🚀 Setup

## Start Docker
docker compose up -d

## Run Backend
cd backend
mvn spring-boot:run

## Run Frontend
cd frontend
npm install
npm run dev

---

# 📡 API Endpoints (Full Documentation)

## 🟢 Create Payment

POST /api/payments

### Request Body
{
  "amount": 1500,
  "currency": "INR",
  "merchantId": "merchant_001",
  "customerId": "cust_900",
  "paymentMethod": "CARD",
  "idempotencyKey": "txn_001"
}

### Response
{
  "id": 1,
  "amount": 1500,
  "currency": "INR",
  "status": "CREATED"
}

---

## 🔵 Get All Payments

GET /api/payments

### Response
[
  {
    "id": 1,
    "amount": 1500,
    "currency": "INR",
    "status": "CREATED"
  }
]

---

## 🟡 Get Payment by ID

GET /api/payments/{id}

### Example
GET /api/payments/1

### Response
{
  "id": 1,
  "amount": 1500,
  "currency": "INR",
  "status": "SUCCESS"
}

---

## 🔁 Idempotency Behavior

Same idempotencyKey will not create duplicate payment.

Example:
txn_001 → same transaction returned

---

## 🧪 Curl Test

curl -X POST http://localhost:8080/api/payments \
-H "Content-Type: application/json" \
-d '{
  "amount": 1500,
  "currency": "INR",
  "merchantId": "merchant_001",
  "customerId": "cust_900",
  "paymentMethod": "CARD",
  "idempotencyKey": "txn_001"
}'

---

# 👨‍💻 Author

**[Balajee A V](https://avbalajee.vercel.app)**
