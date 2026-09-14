# Global Agriculture — Backend

Spring Boot REST API for the `Global Agriculture` React frontend. Every endpoint here
matches the paths and payload shapes the frontend's `src/api/*.js` files already call —
you shouldn't need to change anything on the frontend side.

## Stack

- Java 17, Spring Boot 3.3.4
- Spring Web, Spring Data JPA, Spring Security (used only for BCrypt password hashing —
  see `SecurityConfig.java`; there's no token/session auth, matching the frontend README)
- MySQL 8
- **No Lombok** — every entity/DTO has hand-written getters/setters/constructors, so
  there's nothing extra to install into your IDE. Import into STS and run — no plugin
  setup required.

## 1. Import into Spring Tool Suite

1. Open STS → **File → Import → Maven → Existing Maven Projects**.
2. Browse to this `global-agriculture-backend` folder and finish the import.
3. Let STS download dependencies (first import takes a minute or two).

## 2. Set up MySQL

Create the database (or let the app create it automatically — the JDBC URL already has
`createDatabaseIfNotExist=true`):

```sql
CREATE DATABASE global_agriculture;
```

Then open `src/main/resources/application.properties` and set your own MySQL
username/password:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

Tables are created automatically on first run (`spring.jpa.hibernate.ddl-auto=update`) —
you don't need to run `schema-reference.sql` by hand; it's there purely as documentation
of the shape of the schema.

## 3. Run it

From STS: right-click the project → **Run As → Spring Boot App**.

From the terminal:

```bash
mvn spring-boot:run
```

The API starts on **http://localhost:8080**. On first run, `DataSeeder.java` seeds:

- An admin account — **admin@globalagriculture.com / admin123** (this is the seeded
  account the frontend README tells you to use to reach `/admin`)
- 7 produce categories
- A few sample products (matching some of the images already in the frontend's
  `public/images/products` folder)

## 4. Run the frontend against it

In the frontend project:

```bash
npm install
npm run dev
```

Vite's dev proxy forwards every API path to `localhost:8080`, so just make sure this
backend is running first. No frontend code changes are needed.

## Endpoint reference

| Frontend file | Method & path | Notes |
|---|---|---|
| `api/auth.js` | `POST /api/auth/register` | Always creates a `BUYER` |
| | `POST /api/auth/login` | Returns the user (password never serialized) |
| `api/products.js` | `GET /api/products` | |
| | `GET /api/products/{id}` | |
| | `PUT /api/products/{id}/stock` | body `{ stock }` |
| | `POST /admin/products` | create listing |
| | `PUT /admin/products/{id}` | update listing |
| | `DELETE /admin/products/{id}` | |
| `api/categories.js` | `GET /categories` | |
| `api/cart.js` | `GET /cart/buyer/{buyerId}` | |
| | `POST /cart` | body `{ buyerId, productId, quantity }`, merges into existing row |
| | `PUT /cart/{cartId}` | body `{ quantity }` |
| | `DELETE /cart/{cartId}` | |
| | `DELETE /cart/buyer/{buyerId}` | clears cart |
| `api/orders.js` | `POST /orders` | body `{ buyerId, shippingAddress }` — snapshots cart into order + order_items, decrements stock, opens a PENDING shipment |
| | `GET /orders/buyer/{buyerId}` | |
| | `GET /orders` | all orders (admin) |
| | `GET /order-items/order/{orderId}` | |
| `api/payments.js` | `POST /payments` | body `{ orderId, paymentMethod, transactionId, amount }` — flips order to CONFIRMED/PAID (or PENDING for COD) |
| `api/shipping.js` | `GET /shipping/buyer/{buyerId}` | |
| | `GET /shipping` | all shipments |
| `api/reviews.js` | `GET /reviews` | read-only, as the frontend expects |
| `api/wishlist.js` | `GET /api/wishlist/buyer/{buyerId}` | |
| | `POST /api/wishlist` | body `{ buyerId, productId }` |
| | `DELETE /api/wishlist/buyer/{buyerId}/product/{productId}` | |
| `api/notifications.js` | `GET /api/notifications/user/{userId}` | |
| | `GET /api/notifications/user/{userId}/count` | returns `{ count }` |
| | `PUT /api/notifications/{id}/read` | |
| | `PUT /api/notifications/user/{userId}/read-all` | |
| | `DELETE /api/notifications/{id}` | |
| `api/admin.js` | `GET /admin/users` | |
| | `GET /admin/dashboard/statistics` | returns `{ totalUsers, totalProducts, totalOrders, totalRevenue }` |

## CORS

Every controller carries `@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})`,
and there's a global `CorsConfig`/`SecurityConfig` doing the same — this was flagged as
a gap in the frontend README (`CategoryController`, `ReviewController`, etc. having no
`@CrossOrigin`), so it's fixed everywhere here from the start.

## Error format

All errors come back as JSON: `{ "status": 400, "message": "...", "timestamp": "..." }`.
The frontend's axios interceptor (`src/api/axios.js`) already reads `error.response.data.message`,
so no frontend changes are needed here either.

## What's not implemented (matches the frontend's own notes)

- No review-creation endpoint (frontend only calls `GET /reviews`)
- No "update profile" endpoint (frontend's Profile page is read-only for address/phone)
- No self-service farmer/admin signup — every registration is a `BUYER`

If you want any of these added (e.g. `POST /reviews`, `PUT /users/{id}` for profile
editing, or farmer accounts), let me know and I'll extend the controllers/services.
