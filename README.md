# Payment Service

## Overview

The Payment Service integrates with Stripe to handle secure payment processing for ticket purchases. It creates checkout sessions, receives and verifies webhook events from Stripe, updates order status based on payment outcomes, coordinates atomic inventory decrements with the order service to prevent overselling, and maintains a complete audit trail of all payment events.

## Technology Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.2.x
- **Build Tool:** Maven 3.9.6
- **Database:** PostgreSQL 16
- **Migration:** Flyway
- **Payment Gateway:** Stripe Java SDK
- **Testing:** JUnit 5

## Architecture

The service follows a layered architecture pattern:

```
Controller → Service → Repository → Database
```

### Key Components

- **Controllers:** Handle HTTP requests and responses
- **Services:** Contain business logic and orchestration
- **Repositories:** Data access layer using Spring Data JPA
- **Entities:** Domain models mapped to database tables
- **DTOs:** Data transfer objects for API contracts
- **Exceptions:** Custom exceptions with global exception handling

## Business Flow

### Phase 1: Checkout Session Creation

**Endpoint:** `POST /api/orders`

1. Buyer submits ticket purchase request
2. System validates tier status, inventory, and quantity limits
3. Creates order with PENDING status
4. Creates Stripe checkout session
5. Returns order ID and checkout URL to frontend
6. Buyer completes payment on Stripe-hosted page

### Phase 2: Webhook Event Processing

**Endpoint:** `POST /api/payments/webhook`

1. Stripe sends webhook event after payment completion
2. System verifies webhook signature
3. Checks idempotency (duplicate event detection)
4. Processes payment success or failure event
5. Coordinates atomic inventory decrement
6. Updates order status (CONFIRMED or FAILED)
7. Logs payment event with full webhook payload

## Database Schema

### payments.payments
- Stores payment records with Stripe payment intent IDs
- One-to-one relationship with orders
- Tracks payment status (PENDING, SUCCEEDED, FAILED)

### payments.payment_events
- Audit trail of all Stripe webhook events
- Stores full webhook payload as JSONB
- Enforces idempotency with unique stripe_event_id

### orders.orders
- Order records with buyer information
- Tracks order status and Stripe session ID
- Links to payment records

### orders.order_items
- Line items for each order
- Snapshots of tier and event details at purchase time

### events.ticket_tiers
- Ticket tier inventory and pricing
- Atomic inventory decrement for oversell prevention

## API Endpoints

### Orders

- `POST /api/orders` - Create order and Stripe checkout session

### Payments

- `POST /api/payments/webhook` - Process Stripe webhook events

### Health

- `GET /health/ready` - Readiness probe
- `GET /health/live` - Liveness probe
- `GET /v1/payment/health` - Health status

## Configuration

### Environment Variables

```bash
# Stripe
STRIPE_SECRET_KEY=sk_test_...
STRIPE_WEBHOOK_SECRET=whsec_...
STRIPE_SUCCESS_URL=http://localhost:3000/orders/success
STRIPE_CANCEL_URL=http://localhost:3000/orders/cancel

# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/ticketing
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
DB_POOL_SIZE=10

# Server
SERVER_PORT=8080
```

## Getting Started

### Prerequisites

- Java 21
- PostgreSQL 16
- Stripe account (test mode)
- Maven 3.9.6 (included via wrapper)

### Local Development

1. **Clone the repository**

2. **Configure environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your Stripe credentials
   ```

3. **Start PostgreSQL**
   ```bash
   docker-compose up postgres -d
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Run tests**
   ```bash
   ./mvnw test
   ```

### Docker Deployment

1. **Build and start all services**
   ```bash
   docker-compose up --build
   ```

2. **Stop services**
   ```bash
   docker-compose down
   ```

3. **View logs**
   ```bash
   docker-compose logs -f payment-service
   ```

## Testing with Stripe

### Test Mode Setup

1. Create a Stripe account at https://stripe.com
2. Navigate to Developers → API keys
3. Copy test mode secret key (sk_test_...)
4. Navigate to Developers → Webhooks
5. Add endpoint: `http://localhost:8080/api/payments/webhook`
6. Select events: `checkout.session.completed`, `checkout.session.async_payment_failed`
7. Copy webhook signing secret (whsec_...)

### Test Cards

- **Success:** 4242 4242 4242 4242
- **Declined:** 4000 0000 0000 0002
- **Insufficient funds:** 4000 0000 0000 9995

### Local Webhook Testing

Use Stripe CLI to forward webhooks to localhost:

```bash
stripe listen --forward-to localhost:8080/api/payments/webhook
```

## API Documentation

OpenAPI specification is available at:
- File: `swagger/payment-openapi.yaml`
- Swagger UI: Import the YAML file into https://editor.swagger.io/

## Security Features

- Webhook signature verification
- HTTPS enforcement in production
- JWT authentication for order creation (header: X-Buyer-Id)
- Environment-based secret management
- Parameterized queries (SQL injection prevention)
- Audit logging with sensitive data masking
- Non-root Docker container user

## Monitoring and Observability

### Logging

- Structured logging with SLF4J/Logback
- Automatic trace ID propagation via MDC
- Audit logs for all payment events
- Security alerts for webhook signature failures

### Metrics

- Webhook processing duration
- Payment success/failure rates
- Order confirmation rates
- Inventory decrement conflicts

### Health Checks

- Liveness probe: `/health/live`
- Readiness probe: `/health/ready`
- General health: `/v1/payment/health`

## Edge Case Handling

- **Duplicate Webhooks:** Idempotency check via stripe_event_id
- **Concurrent Oversell:** Atomic inventory decrement with row count validation
- **Invalid Signatures:** Immediate rejection with security alert
- **Database Failures:** Transaction rollback, Stripe auto-retry
- **Payment Failures:** Order marked FAILED, inventory not decremented

## Performance Considerations

- Webhook processing < 30 seconds (Stripe timeout)
- Database connection pooling (HikariCP)
- Indexed queries for fast lookups
- Async audit logging
- Transaction isolation: READ_COMMITTED

## Contributing

1. Follow Java 21 conventions
2. No Lombok or code generation tools
3. Explicit getters/setters/constructors
4. JUnit 5 tests only (no Mockito)
5. Coverage targets: Instructions ≥90%, Branches ≥90%, Lines ≥95%, Methods ≥95%, Classes 100%

## License

Copyright © 2026 Ticketing Platform

## Support

For issues or questions, contact: support@ticketing.com
