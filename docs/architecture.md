<!-- HEADER -->
<br id="top" />
<p align="center">
  <a href="https://github.com/nednella/echo" target="_blank" rel="noopener noreferrer">
    <img src="./assets/echo-logo-256-light-gradient.svg" width="48" />
  </a>
</p>
<h1 align="center">System Architecture</h1>

> A deep dive into the design decisions and architecture of Echo — why I built it, why it’s built the way it is, and what questions were asked along the way.

The document structure may seem a little weird, but it is infact laid out in the same order the project was built, which makes it easier for me to talk about.

<!-- TODO -->

## Table of contents

- [Motivation](#motivation)
  - ["Why another boring social media app?"](#why-another-boring-social-media-app)
  - [Learning objectives](#learning-objectives)
- [Backend](#backend)
  - [Settling on a language and framework](#settling-on-a-language-and-framework)
  - [Structuring the codebase](#structuring-the-codebase)
  - [Defining the API contract vs. the data model](#defining-the-api-contract-vs-the-data-model)
  - [Defining a consistent error model](#defining-a-consistent-error-model)
  - [Ensuring data integrity through validation](#ensuring-data-integrity-through-validation)

<!-- TODO -->

## Motivation

Like all of my previous projects to date, this one came about with the intention of building on my software engineering knowledge and experience. I wanted to forego another basic CRUD demo, and push myself into end-to-end development, mimicking the complexity, practices and tooling of production-scale applications.

### "Why another boring social media app?"

Nobody needs another social app — but I need one to learn how they’re built!

By choosing a well-understood domain, I was free to explore the underlying systems and architectural decisions that make large-scale applications work:

- I can pull inspiration from familiar products like Twitter or Facebook without reinventing the wheel
- I have a clear benchmark for features and UI/UX expectations
- I can focus on architecture and engineering instead of struggling with product ideas and designs

### Learning objectives

I set out to use this project as a lab of sorts, for learning and experimenting with multiple aspects of software engineering — lots of which would be new to me.

- **General practices**:
  - Build a well-structured codebase, using clear separation of concerns and SOLID principles
  - Focus on longer-term maintainability and reduce time spend on refactors
- **Frontend engineering**: 
  - Build upon my React foundation and improve upon writing reusable, performant components
  - Experiment with different approaches to state and data fetching
  - Enforce code quality through strong linting/formatting rules
  - Explore more frontend tooling, e.g., TanStack Router. Built-in type-safe routing and preloading sound great!
- **Backend engineering**:
  - Build upon my Java language knowledge
  - Learn the Spring Boot framework & the wider Spring ecosystem from the ground up
  - Design and implement a complex relational schema
  - Stray into more advanced SQL & consider database performance
- **Authentication & security**:
  - Build upon understanding of authentication & authorisation fundamentals
  - Learn more about RESTful services and how state is managed in an otherwise stateless environment
- **Code testing**:
  - Design unit and integration test suites, explore code coverage and learn how and when to test different applications layers
- **Git workflow**:
  - Practice team-friendly workflows: proper branching, squash merging for clean commit history and structured releases
- **DevOps & CI/CD**:
  - Build pipelines to automatically run tests and enforce code quality before merges
  - Understand how to manage different development environments
  - Explore tooling for easing developer experience, e.g. Docker

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## Backend

<!-- TODO: discuss authorisation once a request is authenticated (e.g. admin) -->

### Settling on a language and framework

Before starting out on this project, I had long since settled on building a Java Spring Boot backend service. I had previously covered Node Express backend services, and didn't like the lack of structure in the framework. 

Prior to this project I was new to Java. I learned the language initially to cover the [Princeton Algorithms I](https://www.coursera.org/learn/algorithms-part1) online course content. The strictness of a statically typed language (minus the sheer quantity of boilerplate) was really refreshing, coming from JS.

Java and Spring are widely used in industry, and I liked the opinionated structure that the framework itself seemed to offer when I was doing my research. So, I thought I'd pick it up and expand my knowledge base.

### Structuring the codebase

I initially started out on the application using a [package-by-layer](https://medium.com/@akintopbas96/spring-boot-code-structure-package-by-layer-vs-package-by-feature-5331a0c911fe) approach. It's fine for simplicity and pretty much and Spring Boot demo will use this approach. But once you start working on more and more features, you're touching every layer of the application, and it gets messy fast. I sat down and thought about how best to refactor the project.

I researched best practices for monolithic applications and [landed on a feature-based approach](https://github.com/nednella/echo/pull/65), with some kind of by-layer folder organisation within. A bit of a hybrid.

Feature-based layering should really be done by considering what components within the feature are public (API layer) and which parts can be kept internal (package-private). Unfortunately, after all this time, Java doesn't offer a take on sub-packaging, and the structure looked messy, so I combed each feature into its own layered structure.

I refactored with the single-responsibility principle in mind, intending on using the package-private access modifier as the default option. Public access is only used where required (controllers, DTOs, service interfaces, shared components). It's not perfect, but it's good enough. Adding application complexity became much easier and that was my goal.

### Defining the API contract vs. the domain model

I started building out the application by considering what information I wanted to return to the client for a given domain.

Working example: profile requests should return the information you'd expect when visiting a user's page. Their unique identifier, some personalised details specific to the application, when they first joined. Contextual information like follower counts, post counts and relationship status with the viewing user should also be included.

<!-- TODO: include ProfileDTO vs Profile entity code blocks -->

By starting with the public-facing contract and shaping the underlying entities to support, I kept the design clean and logical.

### Defining a consistent error model

I took inspiration from the [Spotify Web API](https://developer.spotify.com/documentation/web-api) on this one. Their API errors are simple, the user receives an error status code and a message describing the issue. I opted to replicate this, but also included `timestamp` and `path` properties for additional context.

<!-- TODO: include ErrorResponse code block -->

Starting with the desired client response makes the implementation straightforward. Any explicitly handled exceptions (using Spring's `@ControllerAdvice` and `@ExceptionHandler` annotations) are formatted into the standardised response object.

Any given application exception should be able to be mapped into a particular HTTP status code and a descriptive error message, so I opted to create error catalogue enums per-feature using a custom `ErrorCode` interface. Each declared enum constant should contain its own HTTP status code, a message template, and the number of arguments that the template expects. This way, a template can be formatted while avoiding runtime errors, and tested against accordingly.

<!-- TODO: include ErrorCode interface, and an example implementation code blocks -->

To propagate errors through the application, a custom `RuntimeException` is used. It accepts a given error code and any expected error message arguments, carrying the HTTP status code and the now-formatted error message to the exception handlers to build the response.

Now, the client can always expect errors in a predictable format, regardless of how they originated, simplifying both application debugging and client integration.

### Ensuring data integrity through validation

This was done using the recommended Spring approach — where possible, client-side inputs passed through the request body are validated at the controllers using bean validation annotations within the mapped request objects. This makes it simple to declare validation messages per-request object and keeps the validation logic tightly coupled to the request objects themselves.

<!-- TODO: example request DTO with validation included code block -->

In some cases, simple controller input validation doesn't suffice. Taking the post replies feature for example, when a post is created in reply to another, the request includes the ID of the parent post. In this case, existence of records are also validated within the application business logic. This way, invalid data never finds its way into the database, even when the request shape looks good.

For cases where the request itself is invalid, and not the contained client inputs, Spring does a good job of raising exceptions. Cases like malformed JSON, unsupported HTTP methods, or mismatched argument types (`MethodArgumentTypeMismatch`, `HttpMessageNotReadable`, `HttpRequestMethodNotSupported`) are all thrown automatically.

With the correct exception handlers in place, annotations for field-level checks, service logic for domain rules and framework exceptions for general request errors all flow back to the client in a predictable format.

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

---

<!-- FOOTER -->
<p align="center">
  <sub><a href="#top">back to the top</a></sub>
</p>