# Fullstack website - TalkTactics

Fullstack website designed to help users learn the most popular words in English. This project contains RESTful backend repository written in Spring / Java.

## API Documentation

https://talk-tactics-backend-springboot.onrender.com/swagger-ui/index.html#/

## Technologies Used

### Backend

- Java: A general-purpose programming language used for backend development.
- Spring Boot: A framework that simplifies the development of Java applications, including web applications.
- Spring Security: A powerful and highly customizable security framework for Java applications.
- PostgreSQL: A relational database management system used for storing and retrieving data.
- JSON Web Tokens (JWT): A method for securely transmitting information between parties as a JSON object.
- Hibernate: An object-relational mapping framework for Java applications.
- Swagger UI: A tool for generating interactive API documentation.
- Mockito: A mocking framework for testing Java applications.

## Folder structure:

```bash
com
└── example
    └── talktactics
        ├── auth - All logic related to authentication and authorization
        ├── common - Shared components
        ├── config - Security, WebMvc and Application configuration
        ├── controller - Endpoints for requests
        ├── dto - data transfer objects
        ├── entity - Entity classes
        ├── exception - Custom exceptions
        ├── repository - JPA repositories for SQL requests
        ├── service - Business logic
        └── util - Helpers
```

## Prerequisites

1. Install Docker on your machine.
2. Ensure Maven is installed.
3. Make sure you have installed Java. I used Java 17.

## How to start

1. Clone repository: `git clone https://github.com/PiotrPabichCode/talk-tactics-backend-springboot.git`
2. Configure Secrets

```bash
DB_HOST=_YOUR_DATABASE_HOSTNAME
DB_PORT=_YOUR_DATABASE_PORT
DB_USERNAME=_YOUR_DATABASE_USERNAME
DB_PASSWORD=_YOUR_DATABASE_PASSWORD
DB_DATABASE=_YOUR_DATABASE_NAME
JWT_SECRET_KEY=_RANDOM_JWT_SECRET_KEY
JWT_TOKEN_EXPIRATION=_EXPIRATION_IN_MS
JWT_REFRESH_TOKEN_EXPIRATION=_REFRESH_EXPIRATION_IN_MS
```

3. Update the `application.yml` properties file located in the backend directory to match your specific needs.
4. Open a command-line interface (CLI) or terminal and navigate to the backend directory of the project.
5. Install all dependencies from `pom.xml` file
6. Locate the `TalkTacticsApplication.java` file in the backend source code and run it.
7. The backend server is now up and running, ready to handle incoming requests.
