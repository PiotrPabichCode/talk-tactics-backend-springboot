# Fullstack website - TalkTactics

This project is a fullstack website designed to help users learn the most popular words in English. The website provides a user-friendly interface for improving vocabulary skills and expanding English language knowledge.

## Live demo

https://github.com/PiotrPabichCode/talk-tactics-fullstack/assets/87279370/b664e0a9-5e32-46a3-82fd-08b290611c73

## Technologies Used

### Frontend
- ReactJS: A popular JavaScript library for building user interfaces.
- React Router: A routing library for handling navigation within a React application.
- Axios: A JavaScript library used for making HTTP requests to fetch data from the backend.
- i18next: A powerful internationalization framework for JavaScript applications.
### Backend
- Java: A general-purpose programming language used for backend development.
- Spring Boot: A framework that simplifies the development of Java applications, including web applications.
- Spring Security: A powerful and highly customizable security framework for Java applications.
- PostgreSQL: A relational database management system used for storing and retrieving data.
- JSON Web Tokens (JWT): A method for securely transmitting information between parties as a JSON object.

## Folder structure:

### Frontend
```bash
├───public
│   └───assets
│       └───locales
│           ├───en - JSON english internationalization file
│           └───pl - JSON polish internationalization file
└───src
    ├───api - helpers and API communication files
    ├───assets - global css and image files
    ├───components 
    │   ├───CustomToast
    │   ├───Footer
    │   └───Navbar
    ├───context
    └───pages - UI page components
        ├───
        │   ├───courses - admin-course pages
        │   │   ├───hooks - custom hooks for admin-course pages
        │   │   └───utils - helpers for admin-course pages
        │   ├───users - admin-user pages
        │   │   └───hooks - custom hooks for admin-user pages
        │   └───user_courses - admin-user_course pages
        │       └───hooks - custom hooks for admin-user_course pages
        ├───auth - sign-in, sign-up pages
        ├───courses - course pages
        │   └───hooks - custom hooks for course pages
        ├───home - home page
        └───user - user pages
            └───hooks - custom hooks for user pages
```

### Backend
```bash
com
└── example
    └── talktactics
        ├── DTOs - Data Transfer Objects
        ├── auth - All logic related to authentication and authorization
        ├── config - Security, WebMvc and Application configuration
        ├── controllers - Endpoints for requests
        ├── exceptions - Custom exceptions
        ├── models - Entity classes
        ├── repositories - JPA repositories for SQL requests
        ├── services - Business logic
        └── utils - Helpers
```

## How to start

### Frontend
1. Make sure you have Node.js installed on your system. You can download and install Node.js from the official website: https://nodejs.org.
2. Open a command-line interface (CLI) or terminal and navigate to the frontend directory of the project.
3. Install the project dependencies by running the following command: `npm install`
4. Start development server by running command: `npm start`
5. The development server will start, and you should see console output indicating that the server is running. It will also open your default web browser and navigate to `http://localhost:3000`, where you can access the frontend of the project.
### Backend
1. Ensure you have Java Development Kit (JDK) and PostgreSQL installed on your system.
2. Create an empty database in PostgreSQL that will be used for the project. Make note of the database connection details, including the host, port, database name, username, and password.
3. Update the `application.yml` properties file located in the backend directory to match your specific needs.
4. Open a command-line interface (CLI) or terminal and navigate to the backend directory of the project.
5. Install all dependencies from `pom.xml` file
6. Locate the `TalkTacticsApplication.java` file in the backend source code and run it. 
7. The backend server is now up and running, ready to handle incoming requests.
