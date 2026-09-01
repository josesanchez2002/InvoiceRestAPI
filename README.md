# Invoice Rest API project

Small Rest API applications to manage invoices.

## 🚀 Tech Stack

*   **Java:** 25
*   **Maven:** 3.9.6
*   **Framework:** Spring Boot 4.1.1 with SpringBoot Data / JPA and H2
*   **Build Tool:** Maven
*   **Database:** H2

## 🏗 Architecture & System Design

Pattern: Layered Architecture:Controller / service / repository.

This application is built using the following components:

### Key Components:
* **Controller Layer:** Handles incoming HTTP requests.
  * `InvoiceController`
* **Service Layer:** Contains core business logic.
  * `InvoiceService`
* **Repository Layer:** Manages database interactions using JPA entities.
  * `InvoiceRepository`
  * `TransactionRepository`

## 🛠 Getting Started

### Prerequisites
*   JDK 25 installed
*   Maven 3.9.6 installed

### IDE Setup (IntelliJ)
If you are developing in intelliJ, please make sure to have java 25 and maven 3.9.6 installed

### Running Locally
1.  **Build and run the application:**

    *Using Maven:*
    ```bash
    ./mvnw spring-boot:run 
    ```
    or

    ```bash
    mvn spring-boot:run 
    ```
  
2.  **Access the application:**
    The server will start on `http://localhost:8080`.

3.  **Access the memH2 DB:**
    The server will start on `http://localhost:8080/h2-console`.

## 🧪 Testing

This project uses JUnit 5 and Mockito for testing.

*   **Run unit tests:**
    ```bash
    ./mvnw test  # Maven
    ```
    or
     ```bash
    mvn test  
    ```
## Tasks Completed

* ✅ `GET /invoice/` - returns all invoices
    ```
    curl  http://localhost:8080/invoice
    ``` 
* ✅ `GET /invoice/{invoiceId}` - returns an invoice
    ```
    curl  http://localhost:8080/invoice/31620
    ``` 
* ✅ `GET /invoice/{invoiceId}/status` - returns the invoice status
    ```
    curl  http://localhost:8080/invoice/31620/status
    ``` 
* ⚠️ `POST /invoice` - creates a new invoice
    ```
    curl POST http://localhost:8080/invoice -H "Content-Type: application/json" -d '{"invoiceNumber":"00000", "grossAmount": 100, "gstAmount": 10.0, "netAmount": 10.0, "receiptDate":"2006-04-28T00:00:00", "paymentDueDate":"2006-04-28T00:00:00", "totalNoTransactions": "1"}'
    ```
## Tasks Pending

POST /invoice - creates a new invoice
* ❌ Validation for `POST /invoice`
* ❌ Unit tests
* ❌ Implement pagination for listing invoices. 
* ❌ Implement authentication to secure the endpoints.

# Retrospective
## What went well
* Created the springboot application with no issues
* Task was clear enough and data was provided.
 
## What didn't go well
* After reading the instructions, I proceeded a create the basic project layout (controllers, services, entities). since this project was provided with some seed data,
I made the assumption that this data (invoices and transactions) had to be loaded when the project started.
* This load task took extra time of what I had planned and I even created a custom class to load the csv (using apache common-utils) but since I was using JPA, I had an issue with the transaction and invoice ids.
* Also I had some issues with the Hibernate mapping (given that it was said that there was a one to many relationship between invoices and transactions).

## What have I learnt
* If i had another chance to tackle similar tasks, I'd use Spring JDBC instead to map the relationship manually sine sometimes JPA does not provide a lot of independence.
* i would have clarified the first point about inserting the data at the start of the project or use this data to create JSON payload to first save the invoice and transaction data and then query it.


# Notes
Overall, I had fun working in this little project and even if I didnt complete all the tasks, it gave me a good sense of what I have to improve and have a second thought on how would I would work on it.


