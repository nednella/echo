<!-- HEADER -->
<br id="top" />
<p align="center">
    <a href="https://github.com/nednella/echo" target="_blank" rel="noopener noreferrer">
        <img src="./docs/logo/echo-logo-256-light-gradient.svg" width="48" />
    </a>
</p>
<h1 align="center">Development</h1>

This file provides guidance for getting started with your local development environment when working with code in this repository.


## Table of contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Environment setup](#environment-setup)
- [Environment startup](#environment-startup)
- [Commands](#commands)
- [The onboarding process](#the-onboarding-process)

## Overview

The Echo project is organised as a monorepo consisting of the following components:

- **Authentication:** provided by Clerk
- **Backend:** Java/Spring Boot REST API server with postgreSQL
- **Frontend:** Typescript Vite/React web application

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>


## Architecture

```
echo/
├── .github/                 # GitHub Actions workflows (CI/CD)
│
├── apps/
│   ├── api/                 # Spring Boot backend (REST API, business logic, persistence)
│   │   ├── src/             # Application source code
│   │   ├── build.gradle     # Application build configuration
│   │   └── ...
│   │
│   └── client/              # Frontend web client (TypeScript, React, Vite)
│       ├── src/             # Application source code
│       ├── package.json     # Application dependencies & scripts
│       └── ...
│
├── docs/                    # Documentation andrelated assets
│   ├── assets/              # Documentation images
│   ├── logo/                # Documentation logos
│   ├── architecture.md      # Application architecture discussion
│   └── clerk-setup.md       # Clerk configuration guide
│
├── DEVELOPMENT.md           # Developer setup guide
└── README.md                # Project overview and quick start
```

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## Prerequisites

The project requires a pre-configured Clerk application, as described in the [Clerk setup guide](./docs/clerk-setup.md).

You will require [Java](https://www.oracle.com/europe/java/technologies/downloads/), [Node](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm) and [Docker](https://docs.docker.com/get-started/get-docker/) installations. 

You should use a version manager (e.g. `sdkman` for Java, `nvm` for Node) so that you can easily switch to the required versions.

### Minimum recommended versions

- Java (JDK): **21 (LTS)**
- Node.js: **22.x (LTS, latest patch)**

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## Environment setup

### Clone the repository to your local machine

```sh
git clone https://github.com/nednella/echo.git
cd echo
```

### Environment variables

Each application’s root directory includes an `.env.*.template` file per environment. Copy the template file into a new environment file (as shown below), then fill in any missing values from the appropriate source.

```sh
# Frontend
cp ./apps/client/.env.local.template ./apps/client/.env.local
```

```sh
# Backend
cp ./apps/api/.env.dev.template ./apps/api/.env.dev
```

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## Environment startup

> [!TIP]
> Use multiple terminal tabs to run applications in parallel.


### Start backend

**1. Navigate to the application root**

```sh
cd apps/api
```

**2. Run the application with the local Gradle wrapper**

> [!NOTE]  
> Docker container lifecycles are [automatically managed](https://docs.spring.io/spring-boot/reference/features/dev-services.html) by Spring Boot.

```sh
./gradlew bootRun
```

By default, the server will be available on port `8080`.

### Start frontend

**1. Navigate to the application root**

```sh
cd apps/client
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

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## Commands

### Backend development

```bash
./gradlew bootRun           # start the development server (docker lifecycle management built-in)

./gradlew clean             # clean the /build directory

./gradlew test              # test the application

./gradlew assemble          # build the application without prior testing

./gradlew build             # test and build the application
```

### Frontend development

```bash
npm i                       # Install dependencies

npm run dev                 # Start the development server

npm run build               # build the application

npm run format              # run prettier to check for any formatting errors

npm run format:fix          # run prettier to fix any formatting errors

npm run lint                # run eslint to check for any linting errors

npm run lint:fix            # run eslint to fix any linting errors
```

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## The onboarding process

> [!IMPORTANT]  
> The server requires users to complete the onboarding step before any API calls are accepted. If onboarding is not completed, requests will return `403 Forbidden` with the message `User has not completed the onboarding process`.

Depending on your area of local development, you may or may not need to complete this process manually.

### Backend development

If you are working with the Spring Boot server only, you must create a user in the Clerk dashboard and generate a long-lived session token for authentication.

You can generate a token for a given `User ID` (found via the **Users** tab) using the [Clerk backend API](https://clerk.com/docs/reference/api/overview) from any HTTP client of your choice.

Once you have a valid token, call `POST /v1/clerk/onboarding` once, immediately after registering the user in the dashboard. If the request succeeds, **generate a new session token** (required to pull in updated token claims) and use that token to authenticate subsequent API requests.

### Frontend development

If you register a new user through the web client’s authentication pages, onboarding will be handled automatically via the `/onboarding` page redirect after successful registration.

---

<!-- FOOTER -->
<p align="center">
  <sub><a href="#top">back to the top</a></sub>
</p>
