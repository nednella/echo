<!-- HEADER -->
<br id="top" />
<p align="center">
  <a href="https://github.com/nednella/echo" target="_blank" rel="noopener noreferrer">
    <img src="../docs/logo/echo-logo-256-light-gradient.svg" width="48" />
  </a>
</p>
<div align="center">
  <h1>Spring Boot REST API</h1>
  <p>
    <img alt="Java" src="https://img.shields.io/badge/Java-F71134?style=for-the-badge&logo=openjdk&logoColor=white"/>
    <img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
    <img alt="Spring Security" src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/>
    <img alt="Gradle" src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"/>
    <img alt="Clerk" src="https://img.shields.io/badge/Clerk-5138EE?style=for-the-badge&logo=clerk&logoColor=white"/>
    <img alt="PostgreSQL" src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
    <img alt="Docker" src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
  </p>
</div>

## Table of contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [FAQ](#faq)

## Prerequisites

The following items should be pre-installed on your system:

- Java 21+ JDK
- Docker
- Ngrok, or any tunneling service of your choice

You will also need a pre-configured Clerk application along with the required keys, as described in the [setup guide](../docs/clerk-setup.md).

## Installation

1. **Clone the repository and navigate to the project root**

```
git clone https://github.com/nednella/echo.git
cd echo
```

2. **Navigate to the application**

```
cd api
```

3. **Create your local `.env.dev` file**

```
touch .env.dev
```

4. **Populate your environment variables**

    - Copy the relevant required variables from the template `.env.example`
    - Populate your local `.env.dev` file with the required keys

5. **Run the application locally**

```
./gradlew bootRun
```

The application will run on the default port `8080`. Your Docker containers are automatically managed by Spring (exept shutdown).

## Usage

WIP

## FAQ

WIP

---

<!-- FOOTER -->
<p align="center">
  <sub><a href="#top">back to the top</a></sub>
</p>