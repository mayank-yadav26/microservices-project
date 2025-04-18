# springboot-microservices

## Microservices Included:
- **api-gateway**
- **discovery-server**
- **product-service**
- **inventory-service**
- **order-service**
- **notification-service**
- **k8s**

---

## Database Selection: MongoDB for Product Service, MySQL for Inventory & Order Services

### **Why MongoDB for Product Service?**
- **Schema Flexibility**: Allows storing varied product attributes (color, size, specifications, images) without strict schema constraints.
- **High Read Performance**: Optimized for product browsing and searching with fast querying and indexing.
- **Scalability**: Scales horizontally to handle large product catalogs efficiently.

### **Why MySQL for Inventory & Order Services?**
- **Relational Structure**: Ensures ACID-compliant transactions, crucial for inventory and orders.
- **Joins & Relationships**: Handles relationships between users, products, and transactions effectively.
- **Data Integrity & Consistency**: Prevents stock inconsistencies and overselling with strict transactional support.

### **Hybrid Database Architecture Benefits**
- ✅ **Scalability & Flexibility**: NoSQL (MongoDB) for unstructured product data, SQL (MySQL) for structured transactional data.
- ✅ **Performance Optimization**: MongoDB for fast product catalog queries, MySQL for transactional integrity.
- ✅ **Consistency vs. Availability**: MySQL prioritizes consistency (inventory/orders), MongoDB prioritizes availability (products).

---

## API Gateway

An API Gateway acts as a single entry point for routing client requests to backend services, offering load balancing, security, and monitoring.

### **Why Use API Gateway?**
- **Routing**: Directs requests to appropriate microservices.
- **Security**: Manages authentication & authorization.
- **Load Balancing**: Distributes requests among instances.
- **Rate Limiting**: Prevents excessive API usage.
- **Monitoring & Logging**: Captures request/response data.
- **Protocol Translation**: Supports REST, WebSocket, and other protocols.

### **Advanced Features of Spring Cloud Gateway**
- **Authentication & Authorization**: Integration with Spring Security or JWT.
- **Rate Limiting**: Controls request frequency.
- **Circuit Breaker**: Uses Resilience4j for fault tolerance.
- **Service Discovery**: Integrates with Eureka for dynamic routing.

---

## Discovery Server (Eureka)

### **Role in Microservices Architecture**
- **Eureka Server**: Acts as a service registry for dynamic service discovery.
- **Eureka Client**: Registers services and discovers other services dynamically.

### **Benefits of Service Discovery**
- Eliminates hardcoded service URLs.
- Enables dynamic scaling of microservices.
- Improves fault tolerance and resiliency.

### **URL Differences with Eureka**

| Scenario           | URL Used in Order Service |
|--------------------|--------------------------|
| Without Eureka    | `http://localhost:8082/api/inventory` |
| With Eureka       | `http://inventory-service/api/inventory` |

**Key Benefit:** With Eureka, service URLs remain unchanged even if the service moves to another port or machine.

---

## Product Service

### **Database Used**: MongoDB
- Stores product data efficiently with a flexible schema.
- Optimized for read-heavy operations.

### **Key Properties Stored**:
- `id`
- `name`
- `description`
- `price`

---

## Inventory Service

### **Database Used**: MySQL
- Ensures transactional integrity for inventory stock management.
- Supports relational queries for inventory tracking.

---

## Order Service

### **Database Used**: MySQL
- Guarantees ACID compliance for order transactions.
- Supports complex relationships between users, products, and transactions.

### **Fault Tolerance with Resilience4j**
- **Circuit Breaker**: Prevents system failures by falling back to an alternative method.
- **Time Limiter**: Ensures time-bound execution.
- **Retry Mechanism**: Automatically retries failed requests.

### **Kafka Integration**
- Uses Kafka for event-driven communication.
- Sends order placed events to a `notificationTopic`.

---

## Monitoring & Health Checks

### **Actuator Prometheus Endpoint**
- Enables Prometheus endpoint for monitoring application metrics.
- Exposes system metrics like memory usage, CPU load, and request count.

### **Health Management Properties**
- Enables circuit breaker health monitoring.
- Configures endpoint exposure for system monitoring.


