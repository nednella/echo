<!-- HEADER -->
<br id="top" />
<p align="center">
    <a href="https://github.com/nednella/echo" target="_blank" rel="noopener noreferrer">
        <img src="./docs/logo/echo-logo-256-light-gradient.svg" width="48" />
    </a>
</p>
<h1 align="center">Development</h1>

The Echo project is organised as a monorepo and consists of the following components:

- Authentication provided by Clerk
- A backend written in Java, exposing a REST API
- A frontend written in TypeScript
- A PostgreSQL database

## Project structure

```
echo/
├── .github/                 # GitHub Actions workflows (CI/CD)
│
├── api/                     # Spring Boot backend (REST API, business logic, persistence)
│   ├── src/                 # Application source code
│   ├── build.gradle         # Application build configuration
│   └── ...             
│
├── client/                  # Frontend web client (TypeScript, React, Vite)
│   ├── src/                 # Application source code
│   ├── package.json         # Application dependencies & scripts
│   └── ...
│
├── docs/                    # Documentation and assets
│   ├── assets/              # Project images
│   ├── logo/                # Project logos
│   ├── architecture.md      # Application architecture discussion
│   └── clerk-setup.md       # Clerk configuration guide
│
├── DEVELOPMENT.md           # Developer setup guide
└── README.md                # Project overview and quick start
```

## Prerequisites

The project requires a pre-configured Clerk application, as described in the [Clerk setup guide](./docs/clerk-setup.md).

You will require [Java](https://www.oracle.com/europe/java/technologies/downloads/), [Node](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm) and [Docker](https://docs.docker.com/get-started/get-docker/) installations. 

You should use a version manager (e.g. `sdkman` for Java, `nvm` for Node) so that you can easily switch to the required versions.

### Minimum recommended versions

- Java (JDK): **21 (LTS)**
- Node: **22.x (latest LTS)**

## Setup environment

### Clone the repository to your local machine

```sh
git clone https://github.com/nednella/echo.git
cd echo
```

### Setup environment variables

Each application’s root directory includes an `.env.*.template` file per environment. Copy the values from that file into a new environment file (see below) and fill in any missing values from the appropriate source.

```sh
# Frontend
cp ./client/.env.local.template ./client/.env.local
```

```sh
# Backend
cp ./api/.env.dev.template ./api/.env.dev
```

## Start environment

> [!TIP]
> Use multiple terminal tabs to run applications in parallel.


### Start backend

**1. Navigate to the application root**

```sh
cd api
```

**2. Run the application with the local Gradle wrapper**

> [!NOTE]  
> Your Docker container lifecycles are [automatically managed](https://docs.spring.io/spring-boot/reference/features/dev-services.html) by Spring Boot.

```sh
./gradlew bootRun
```

By default, the server will be available on port `8080`.

### Start frontend

**1. Navigate to the application root**

```sh
cd client
```

**2. Install dependencies**

```sh
npm i
```

**3. Run the application**

```sh
npm run dev
```

By default, the web client will be available on port [5173](http://localhost:5173/).

## The onboarding process

> [!IMPORTANT]  
> The server requires that a user completes the onboarding step before any API calls will be accepted. If onboarding is not completed, requests will return `403 Forbidden` with the message `User has not completed the onboarding process`.

Depending on your area of local development, you may or may not need to complete this process manually.

### Frontend development

If you register a new user through the web client’s authentication pages, onboarding will be handled automatically via the `/onboarding` page redirect after successful registration.

### Backend development

If you are working with the Spring Boot server only, you must create a user in the Clerk dashboard and generate a long-lived session token for authentication.

You can generate a token for a given `User ID` (found via the **Users** tab) from any client of choice using the [Clerk backend API](https://clerk.com/docs/reference/api/overview).

Once you have a valid token, call `POST /v1/clerk/onboarding` once, immediately after registering the user in the dashboard. If the request succeeds, **generate a new session token** (required to pull in updated token claims) and use that token to authenticate subsequent API requests.

---

<!-- FOOTER -->
<p align="center">
  <sub><a href="#top">back to the top</a></sub>
</p>
