<!-- HEADER -->
<br id="top" />
<p align="center">
  <a href="https://github.com/nednella/echo" target="_blank" rel="noopener noreferrer">
    <img src="./assets/echo-logo-256-light-gradient.svg" width="48" />
  </a>
</p>
<div align="center">
  <h1>React Web Client</h1>
  <p>
    <img alt="TypeScript" src="https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white"/>
    <img alt="React" src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB"/>
    <img alt="Vite" src="https://img.shields.io/badge/Vite-B73BFE?style=for-the-badge&logo=vite&logoColor=FFD62E"/>
    <img alt="Prettier" src="https://img.shields.io/badge/prettier-1A2C34?style=for-the-badge&logo=prettier&logoColor=F7BA3E"/>
    <img alt="ESLint" src="https://img.shields.io/badge/eslint-3A33D1?style=for-the-badge&logo=eslint&logoColor=white"/>
    <img alt="TailwindCSS" src="https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white"/>
    <img alt="Clerk" src="https://img.shields.io/badge/Clerk-5138EE?style=for-the-badge&logo=clerk&logoColor=white"/>
    <img alt="TanStack Router" src="https://img.shields.io/badge/TanStack_Router-FF4154?style=for-the-badge"/>
  </p>
</div>

## Table of contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [FAQ](#faq)

## Prerequisites

...

You will need a pre-configured Clerk application along with the required keys, as described in the [setup guide](clerk.md).

## Installation

1. **Clone the repository and navigate to the project root**

```
git clone https://github.com/nednella/echo.git
cd echo
```

2. **Navigate to the application**

```
cd client
```

3. **Install the dependencies**

```
npm i
```

4. **Create your local `.env` file**

```
touch .env
```

5. **Populate your environment variables**

    - Copy the relevant required variables from the template `.env.example`
    - Populate your local `.env` file with the [required keys](clerk.md#obtaining-the-api-keys)

6. **Run the application locally**

```
npm run dev
```

The application will run on the default port `5173`.

## FAQ

WIP

---

<!-- FOOTER -->
<p align="center">
  <sub><a href="#top">back to the top</a></sub>
</p>