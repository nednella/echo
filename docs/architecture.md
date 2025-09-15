<!-- HEADER -->
<br id="top" />
<p align="center">
  <a href="https://github.com/nednella/echo" target="_blank" rel="noopener noreferrer">
    <img src="./assets/echo-logo-256-light-gradient.svg" width="48" />
  </a>
</p>
<h1 align="center">System Architecture</h1>

> A deep dive into the design decisions and architecture of Echo — why I built it, why it’s built the way it is, and what questions were asked along the way.

<!-- TODO -->

## Table of contents

- [Motivation](#motivation)
  - ["Why another boring social media app?"](#why-another-boring-social-media-app)
  - [Learning objectives](#learning-objectives)

<!-- TODO -->

## Motivation

Like all of my previous projects to date, this one came about with the intention of building on my software engineering knowledge and experience. I wanted to forego another basic CRUD demo, and push myself into end-to-end development, mimicking the complexity, practices and tooling of production-scale applications.

### "Why another boring social media app?"

The world doesn’t need another social app — but I needed one to learn how they’re built!

Social platforms are largely a solved problem in the modern world, which makes them the ideal playground:  

- I can pull inspiration from familiar products like Twitter or Facebook without reinventing the wheel
- I have a clear benchmark for features and UI/UX expectations
- I can focus on architecture and engineering instead of struggling with product ideas and designs

By choosing a well-understood domain, I was free to explore the underlying systems and architectural decisions that make large-scale applications work.

### Learning objectives

I set out to use this project as a lab of sorts, for learning and experimenting with multiple aspects of software engineering — lots of which would be new to me.

- **General practices**:
  - Build a well-structured codebase, using clear separation of concerns and SOLID principles
  - Focus on longer-term maintainability and reduce time spend on refactors
- **Frontend engineering**: 
  - Build upon my React foundation and improve upon writing reusable, performant components
  - Experiment with different approaches to state and data fetching
  - Enforce code quality through strong linting/formatting rules
  - Explore modern frontend tooling, e.g., TanStack Router. Built-in type-safe routing and preloading sound great!
- **Backend engineering**:
  - Build upon my Java language knowledge
  - Learn the Spring Boot framework & the wider Spring ecosystem from the ground up
  - Design and implement a complex relational schema
  - Stray into more advanced SQL querying & consider database performance
- **Authentication & security**:
  - Build upon understanding of authentication & authorisation fundamentals
  - Learn about RESTful services and how to manage state in an otherwise stateless environment
- **Code testing**:
  - Design unit and integration test suites, explore code coverage and learn how and when to test different applications layers
- **Git workflow**:
  - Practice team-friendly workflows: proper branching, squash merging for clean commit history and structured releases
- **DevOps & CI/CD**:
  - Build pipelines to automatically run tests and enforce code quality before merges
  - Understand how to manage different development environments
  - Explore tooling for easing developer experience, like Docker

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

---

<!-- FOOTER -->
<p align="center">
  <sub><a href="#top">back to the top</a></sub>
</p>