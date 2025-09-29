<!-- HEADER -->
<br id="top" />
<p align="center">
  <a href="https://github.com/nednella/echo" target="_blank" rel="noopener noreferrer">
    <img src="./logo/echo-logo-256-light-gradient.svg" width="48" />
  </a>
</p>
<h1 align="center">System Architecture</h1>

> A deep dive into the design decisions and architecture of Echo — why I built it, why it’s built the way it is, and what questions were asked along the way.

## Table of contents

<!-- TODO: validate table links once complete -->
<!-- TODO: validate doc links once complete -->

- [Motivation](#motivation)
  - ["Why another boring social media app?"](#why-another-boring-social-media-app)
  - [Learning objectives](#learning-objectives)
- [Backend](#backend)
  - [Settling on a language and framework](#settling-on-a-language-and-framework)
  - [Structuring the codebase](#structuring-the-codebase)
  - [Defining the API contract vs. the domain model](#defining-the-api-contract-vs-the-domain-model)
  - [Defining a consistent error model](#defining-a-consistent-error-model)
  - [Ensuring data integrity through validation](#ensuring-data-integrity-through-validation)
  - [Choosing a DBMS](#choosing-a-dbms)
  - [Mixing data access methods](#mixing-data-access-methods)
  - [The trade-offs of using Spring JDBC](#the-trade-offs-of-using-spring-jdbc)
  - [Testing the application](#testing-the-application)
- [Authentication & security](#authentication--security)
  - [Starting with username-password form authentication](#starting-with-username-password-form-authentication)
  - ["Why switch to Clerk, then?"](#why-switch-to-clerk-then)
  - [The challenge of synchronising databases](#the-challenge-of-synchronising-databases)
- [Frontend](#frontend)
- [DevOps & CI/CD](#devops--cicd)
  - [Git workflow](#git-workflow)
  - [Conventional commits](#conventional-commits)
  - [Branch protection rules](#branch-protection-rules)
  - [Continuous integration](#continuous-integration)
  - [Continuous deployment](#continuous-deployment)
  - [Deployment services](#deployment-services)
- [Finishing up](#finishing-up)

## Motivation

Like all of my previous projects to date, this one came about with the intention of building on my software engineering knowledge and experience. I wanted to forego another basic CRUD demo, and push myself into end-to-end development, mimicking the complexity, practices and tooling of production-scale applications.

### "Why another boring social media app?"

I need one to learn how they’re built!

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
  - Explore tooling for improved developer experience, e.g. Docker

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## Backend

<!-- TODO: discuss authorisation once a request is authenticated (e.g. admin) -->

### Settling on a language and framework

Before starting out on this project, I had long since settled on building a Java Spring Boot backend service. I had previously covered Node Express backend services, and didn't like the lack of structure in the framework. 

Prior to this project, I was new to Java. I learned the language initially to cover the [Princeton Algorithms I](https://www.coursera.org/learn/algorithms-part1) online course content. The strictness of a statically typed language (minus the sheer quantity of boilerplate) was really refreshing, coming from JS.

Java and Spring are widely used in industry, and I liked the opinionated structure that the framework itself seemed to offer when I was doing my research. So, I thought I'd pick it up and expand my knowledge base.

### Structuring the codebase

I initially started out on the application using a [package-by-layer](https://medium.com/@akintopbas96/spring-boot-code-structure-package-by-layer-vs-package-by-feature-5331a0c911fe) approach. It's fine for simplicity and pretty much any Spring Boot demo will use this approach. But once you start working on more and more features, you're touching every layer of the application, and it gets messy fast. I sat down and thought about how best to refactor the project.

I researched best practices for monolithic applications and [landed on a feature-based approach](https://github.com/nednella/echo/pull/65), with some kind of by-layer folder organisation within. A bit of a hybrid.

Feature-based layering should really be done by considering what components within the feature are public (API layer) and which parts can be kept internal (package-private). Unfortunately, after all this time, Java doesn't offer a take on sub-packaging, and the structure looked messy, so I combed each feature into its own layered structure.

I refactored with the single-responsibility principle in mind, intending on using the package-private access modifier as the default option. Public access is only used where required (controllers, DTOs, service interfaces, shared components). It's not perfect, but it's good enough. Adding application complexity became much easier and that was my goal.

### Defining the API contract vs. the domain model

I started building out the application by considering what information I wanted to return to the client for a given domain.

#### Working example

Profile requests should return the information you'd expect when visiting a user's page. Their unique identifier, some personalised details specific to the application, and when they first joined. Contextual information like follower counts, post counts and relationship status with the viewing user should also be included.

```javascript
{
    "id": String,
    "username": String,
    "name": String | null,
    "bio": String | null,
    "location": String | null,
    "image_url": String | null,
    "created_at": String,
    "metrics": { ... },
    "relationship": { ... } | null
}
```

By starting with the public-facing contract and shaping the underlying entities to support, I kept the models clean and logical.

### Defining a consistent error model

I took inspiration from the [Spotify Web API](https://developer.spotify.com/documentation/web-api) on this one. Their API errors are simple, the user receives an error status code and a message describing the issue. I opted to replicate this, but also included `timestamp` and `path` properties for additional context.

```java
public record ErrorResponse(
    Instant timestamp,
    int status,
    String message,
    String path
) {}
```

Starting with the desired client response makes the implementation straightforward. Any explicitly handled exceptions (using Spring's `@ControllerAdvice` and `@ExceptionHandler` annotations) are formatted into the standardised response object.

Any given application exception should be able to be mapped into a particular HTTP status code and a descriptive error message, so I opted to error enums per-feature implemented against an `ErrorCode` interface. Each declared error should contain its own HTTP status code, a message template, and the number of arguments that the template expects. This way, a template can be formatted while avoiding runtime errors, and tested against accordingly.

```java
public interface ErrorCode {

    HttpStatus getStatus();

    String getMessageTemplate();

    int getExpectedArgs();

    default String formatMessage(Object... args) {
        ...
    }

    default ApplicationException buildAsException(Object... args) {
        ...
    }

}
```

Now, the client can always expect errors in a predictable format, regardless of how they originated, simplifying both application debugging and client integration.

### Ensuring data integrity through validation

This was done using the recommended Spring approach — where possible, client-side inputs passed through the request body are validated at the controllers using bean validation annotations within the mapped request objects. This makes it simple to declare validation messages per-request object and keeps the validation logic tightly coupled to the request objects themselves.

In some cases, simple controller input validation doesn't suffice. Taking the post replies feature for example, when a post is created in reply to another, the request includes the ID of the parent post. In this case, existence of records are also validated within the application business logic. This way, invalid data never finds its way into the database, even when the request shape looks good.

Finally, the database schema itself acts as a last wall of defence. Constraints and indexes ensure that even if bad data splips through, it cannot be persisted.

For cases where the request itself is invalid, and not the contained client inputs, Spring does a good job of raising exceptions. Cases like malformed JSON, unsupported HTTP methods, or mismatched argument types (`MethodArgumentTypeMismatch`, `HttpMessageNotReadable`, `HttpRequestMethodNotSupported`) are all thrown automatically.

With the correct exception handlers in place, annotations for field-level checks, service logic for domain rules and framework exceptions for general request errors all flow back to the client in a predictable format.

### Choosing a DBMS

Not much to talk about here. I knew before starting out that a relational model would be best suited. The project has a rigid data structure, and most `GET` requests would involve stitching various structures together. During research into the various options, Postgres came up as the most widely recommended option.

### Mixing data access methods

Spring offers [various approaches](https://docs.spring.io/spring-framework/reference/data-access.html) to data access within your application. You have [full-feature ORMs](https://docs.spring.io/spring-framework/reference/data-access/orm.html) like Hibernate and JPA, and some barebones abstractions like [Spring JDBC](https://docs.spring.io/spring-framework/reference/data-access/jdbc.html). 

For simple CRUD operations, I leaned on Spring Data JPA's [repository interfaces](https://docs.spring.io/spring-data/jpa/reference/repositories/definition.html). The basics are covered very well with these, and it's easy to [define custom query methods](https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html) if the defaults don't suffice. The best part is that, since JPA generates the query method implementations, you don't need to write any integration tests.

Complexity grew when working on `GET` requests for profiles and posts. These endpoints required complex data transfer objects (DTO) that combined data from multiple tables. Taking a look at a standard representation of a user's post:

```javascript
{
    "id": String,
    "parent_id": String | null,
    "conversation_id": String,
    "author": {
        "id": String,
        "username": String,
        "name": String | null,
        "image_url": String | null,
        "relationship": { ... } | null
    },
    "text": String,
    "created_at": String,
    "metrics": { ... },
    "relationship": { ... },
    "entities": { ... }
}
```

It doesn't just expose a record in the `post` table; there's a lot of additional context such as author details, post metrics, relationships and entities to give the client full context.

At the time, I couldn't figure out how to model these requirements through an ORM alone. Instead, I discovered that if I used Spring JDBC, I could write SQL directly and map the resulting rows into the required shape using a `RowMapper`. This way, I could focus on writing efficient SQL functions that covered my requirements.

### The trade-offs of using Spring JDBC

There were positives to draw from the decision. Writing raw SQL forced me to understand it better and expand my knowledge. I became more comfortable joining and aggregating data, and explored new-to-me features like views and CTEs. By writing the queries myself, I could also easily look at query performance using `EXPLAIN ANALYZE`.

But, there are some significant hindrances. Refactoring data access is a headache, and debugging raw SQL is a nightmare. If I touch a base query function like `fetch_posts`, I will have to carefully update every higher-level function that consumes it. If I introduce a bug in a query, I don't have any IDE support, and I've found that SQL errors are not the most descriptive.

It works, but it's very easy to make mistakes and frustrating to maintain.

### Testing the application

I kept the backend tests pragmatic. Fast unit tests for behaviour, and a smaller set of integration tests to prove the application wiring (filters → controllers → services → repositories).

#### Unit testing

At the unit level, services are tested in isolation with dependencies mocked with Mockito so I can focus on business rules and exception handling. Controllers are tested with `@MockMvcTest` to ensure request validation and error mapping. Smaller components like mappers and utility functions are also unit tested. Essentially, any code that can be isolated and tested against, is.

#### Integration testing

For integration testing, I use [Testcontainers](https://testcontainers.com/) to spin up fresh Postgres instances alongside either a full Spring context (`@SpringBootTest`), or a narrower repository slice (`@DataJpaTest`). HTTP endpoints are tested with Spring's [WebTestClient](https://docs.spring.io/spring-framework/reference/testing/webtestclient.html) by sending real valid/invalid requests through the filter chain, controllers, services and persistence layer, covering:

- Authentication and security filters
- Input validation
- Business logic
- Database integrity

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## Authentication & security

### Starting with username-password form authentication

The application revolves around users and interactions between those users, and the groundwork for those social interactions starts with authentication. Developing features before authentication didn't make sense when the app revolves around knowing *who* the authenticated user is.

When I began working on the backend, I spent time learning the Spring framework, including Spring Security, a trusted and highly customisable authentication and access-control framework. It supports [numerous popular authentication mechanisms](https://docs.spring.io/spring-security/reference/servlet/authentication/index.html) with minimal setup required.

I started with the simplest: username–password authentication. With minimal configuration, you can have:

- A robust authentication implementation backed by a custom `user` table that stores usernames and BCrypt hashed passwords
- [In-memory session persistence](https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html) using configurable session cookies
- Custom authentication endpoints (e.g. `/auth/login`, `/auth/logout`) instead of the default Spring login page
- Options for external cache or database integration such as [Redis](https://docs.spring.io/spring-session/reference/http-session.html), for horizontal scaling

This approach met my early development requirements and allowed me to start layering on application features.

### "Why switch to Clerk, then?"

The long-term goal was to support both username–password authentication with the usual email verification “forgot password” flows, and OAuth integration with providers like GitHub and LinkedIn.

While I could have extended my Spring Security setup and built out additional security features, it's deceptively complex to go beyond the basics, and it's not recommended to do this from scratch. It's much more straightforward to integrate with 3rd party authentication libraries or providers, and rolling your own auth only once the application scale requires you to do so.

I compared several popular solutions from libraries to hosted providers — including Auth.js, Better Auth, Lucia, Auth0, Clerk and Supabase — and considered the following points:

- **Frontend integration**: Is there a plug-and-play TypeScript SDK? Are there pre-built UI components?
- **Database control**: Can I manage my own `user` table, and if not, how do I maintain database sync?
- **Developer experience**: Are the docs reliable? Is there support if I run into issues? What about the risk of vendor lock-in?
- **Cost**: Is there a free tier for development? What happens if the project grows unexpectedly?

In the end, I chose [Clerk](https://clerk.com/). Their developer experience is second-to-none: the dashboard UI is one of the best, the React integration is seamless, the docs answer most questions, and I can personally vouch that their email support fills the gaps. Their free tier plan covers 10,000 monthly active users, with a unique definition of "active" — a user who comes back 24+ hours after registration, and not just uses the application once. Within a few minutes of setup, I had production-grade authentication running on the client, issuing session tokens that my API could validate against.

### The challenge of synchronising databases

The trade-off for integrating with Clerk was losing control of the `user` table — Echo's entire social model depends on referencing a local `user` table!

Clerk currently does not offer exposure to their underlying database, nor do they provide triggers to synchronise state directly. What they do offer (at the time of writing) is asynchronous, non-guaranteed [webhook](https://clerk.com/docs/webhooks/sync-data) events.

Webhooks are useful for maintaining sync of PUTs and DELETEs, but not for critical actions like user registration. A new user should immediately be able to access and navigate the application without error, *guaranteed*. This requires the user to be present in the local database immediately after the point of registration.

Clerk discusses an ["onboarding flow"](https://clerk.com/blog/add-onboarding-flow-for-your-application-with-clerk) that you can use to collect key information from your user post-registration, and use that information to drive application state. [I chose to adapt this flow with a twist](https://github.com/nednella/echo/pull/61). Rather than collecting information from the user, an onboarding page will automatically send an authenticated request to the server as soon as registration completes. From that request, I can run a database upsert:

- If a user doesn't exist with the given Clerk ID, create one, and update the Clerk user as having completed the onboarding process
- If a user already exists, update (or skip)

Once the server responds OK, the client can safely redirect the user to the core application, and the server can rely on webhooks for post-registration updates.

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## Frontend

WIP

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## DevOps & CI/CD

### Git workflow

The repository follows [trunk-based development](https://trunkbaseddevelopment.com/) using short-lived feature branches.

- All new work starts from the latest version of `main`
- Branches are kept small and focussed on a specific change
- Once a change is ready, a PR is opened back into `main`

This way, `main` acts as the single source of truth and is always in a deployable state.

### Conventional commits

To standardise commit messages, the repository uses the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) spec. There are a couple key benefits:

1. **Readable history** - the log explains clearly what changes were made
2. **Automation** - tools can parse commit messages to generate version bumps and changelogs

### Branch protection rules

The highlighted workflow is enforced with specific branch protection rules.

- A pull request is required before merging
- A linear commit history is required (squash merges only -- required for automation tooling)
- Specific status checks must pass before permitting a merge

Checks include a `check-pr-title` job that enforces the Conventional Commits spec, and CI workflows for each application. As a result, every change that lands on `main` is meaningful and production-ready.

### Continuous integration

CI runs via GitHub Actions for every PR into `main`. Each application has it's own dedicated CI workflow that involves formatting, linting and testing the source code.

To save time, CI workflows are [conditionally executed](https://github.com/dorny/paths-filter) based on whether the relevant source code was touched.

### Continuous deployment

Application deployment workflows run via GitHub Actions on tag pushes.

The deployment process is automated with [release-please](https://github.com/googleapis/release-please). The tool works by parsing commits on a given branch (in this case, `main`) and looks for messages that match the [Conventional Commits](#conventional-commits) spec. When qualifying changes are detected, it automatically opens a **"Release PR"** with an updated changelog and a [semantic version](https://semver.org/) bump.

Upon merging a release PR, the tool executes the following tasks:

- Updates the `CHANGELOG.md` file
- Updates the relevant files versioned files (e.g., `package.json`, `build-gradle`)
- Tags the commit with the updated application version number
- Creates a GitHub release based on this tag

The tool is configured to look at each application individually using the [manifest-driven configuration](https://github.com/googleapis/release-please/blob/main/docs/manifest-releaser.md). As a result, each application maintains its own changelog and version, allowing deployment on a per-application basis.

From there, GitHub Actions picks up a pushed tag and runs the relevant deployment pipeline. The end result is that deployment for a given application within the project requires no manual intevention, except one click on the green button within the release PR.

The process is entirely hands-off and reliable when matched with the afore mentioned branch protection ruleset. Implementing this tool was probably the best thing I did for myself throughout the entire project.

### Deployment services

Not much to discuss here. This is really just a learning project and I don't expect to generate any organic traffic, so it's hosted with services that either have a generous free tier, or at most, a cheap but reliable hobby tier.

The web client is currently hosted with [Vercel](https://vercel.com/home), and the backend service is hosted with [Railway](https://railway.com/).

<p align="right">
  <sub><a href="#top">back to the top</a></sub>
</p>

## Finishing up

WIP

---

<!-- FOOTER -->
<p align="center">
  <sub><a href="#top">back to the top</a></sub>
</p>