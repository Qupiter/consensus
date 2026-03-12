# Consensus

Consensus is a **Java-based backend platform** for creating and managing voting systems.
It follows **Domain-Driven Design (DDD)** principles and a **component-driven architecture**, making it modular, maintainable, and scalable.

---

## 🚀 Features (Initial Version)

- User registration and login (Auth module)
- Secure password hashing with BCrypt
- Clean project skeleton using DDD
- Shared configuration ready for JWT and other modules

## ⚙️ Tech Stack

- Java 21
- Spring Boot
- PostgreSQL (via JPA/Hibernate)
- Maven
- Docker (for DB and future deployment)
- JUnit 5 + Testcontainers (testing)

---

## 🏗 Getting Started

### Prerequisites

- Java 21+
- Maven
- PostgreSQL or Docker for DB

### Run Locally

1. Clone the repository:

`
git clone https://github.com/<your-username>/consensus.git
cd consensus
`

2. Start PostgreSQL (Docker recommended):

`docker run --name consensus-db -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres`

3. Build and run the app:

`
mvn clean install
mvn spring-boot:run
`

4. API endpoints:

- POST /auth/register → Register new user
- POST /auth/login → Login

---

## 📝 License

This project is **open-source** under the **MIT License**.  
See LICENSE for details.

---

## ⚡ Contribution

- Fork the repository
- Create a new branch (feature/your-feature)
- Commit your changes (git commit -m "Add new feature")
- Push to the branch (git push origin feature/your-feature)
- Open a Pull Request
