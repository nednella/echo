# Changelog

## [0.4.1](https://github.com/nednella/echo/compare/api@v0.4.0...api@v0.4.1) (2025-11-20)


### Bug Fixes

* **docs:** add note to highlight default dummy clerk webhook signing secret ([#166](https://github.com/nednella/echo/issues/166)) ([1585c0d](https://github.com/nednella/echo/commit/1585c0d2667d16cb87c0ddc98a1dc01f69c53f26))
* **docs:** correct spelling/wording errors and numerous broken hyperlinks/references ([#163](https://github.com/nednella/echo/issues/163)) ([4cf1a0c](https://github.com/nednella/echo/commit/4cf1a0c459fd68af5f3ad7bd378da0712ecbe42b))

## [0.4.0](https://github.com/nednella/echo/compare/api@v0.3.1...api@v0.4.0) (2025-11-11)


### Features

* **api:** add flyway db migrations ([#151](https://github.com/nednella/echo/issues/151)) ([7a2d786](https://github.com/nednella/echo/commit/7a2d786841af13a3af713c47ed6f26439ffa1568))
* **api:** add flyway plugin ([#161](https://github.com/nednella/echo/issues/161)) ([bb343f8](https://github.com/nednella/echo/commit/bb343f866fe8dd3766f038fb0ac6188e3a27fabb))
* **api:** add OAuth2AuthenticationException handler ([#134](https://github.com/nednella/echo/issues/134)) ([8b15492](https://github.com/nednella/echo/commit/8b15492e01d3c468a252af72e32cd3e6e32db773))
* **api:** annotate ErrorResponse with NotNull so OpenAPI spec infers as required fields ([#130](https://github.com/nednella/echo/issues/130)) ([4c5f158](https://github.com/nednella/echo/commit/4c5f15820db43a16604cb837be91d99f7fc1ea18))
* **api:** improve db repeatables ([#155](https://github.com/nednella/echo/issues/155)) ([f1931f2](https://github.com/nednella/echo/commit/f1931f296d866204f9ca520c5723d30c7d6ee229))
* **api:** remove unused user status enum ([#154](https://github.com/nednella/echo/issues/154)) ([dd24980](https://github.com/nednella/echo/commit/dd24980bd988657bed098bfc945e70b2402f7642))
* **api:** update controller OpenAPI documentation ([#128](https://github.com/nednella/echo/issues/128)) ([9dd81d1](https://github.com/nednella/echo/commit/9dd81d161bb3f682afe5bf0c1be9df493f0a47ab))


### Bug Fixes

* **api:** de-duplicate profile mentions ([#156](https://github.com/nednella/echo/issues/156)) ([8f73e28](https://github.com/nednella/echo/commit/8f73e28a8e7d50c7296d51a98815e748207d8dfe))
* **api:** ensure posts are cascade deleted on author_id, conversation_id, parent_id ([#158](https://github.com/nednella/echo/issues/158)) ([b46233e](https://github.com/nednella/echo/commit/b46233e988ab03ee8da5b67fef558c2797188091))
* **api:** update profile lookups and mentions to use case-insensitive usernames ([#157](https://github.com/nednella/echo/issues/157)) ([ddd9d5b](https://github.com/nednella/echo/commit/ddd9d5bb69aecf40beda8a52282748d915d8011e))


### Performance Improvements

* **api:** reduce application idle memory consumption ([#162](https://github.com/nednella/echo/issues/162)) ([95833b0](https://github.com/nednella/echo/commit/95833b05a08f8983f58cfbc6570b49ae8ee71d8d))

## [0.3.1](https://github.com/nednella/echo/compare/api@v0.3.0...api@v0.3.1) (2025-09-30)


### Bug Fixes

* **release:** force patch version bump ([#121](https://github.com/nednella/echo/issues/121)) ([882f08e](https://github.com/nednella/echo/commit/882f08e3956cf1b44f3491ad79f38d3aa8db03f1))

## [0.3.0](https://github.com/nednella/echo/compare/api@v0.2.0...api@v0.3.0) (2025-09-29)


### Features

* **api:** add openapi docs  ([#107](https://github.com/nednella/echo/issues/107)) ([a918406](https://github.com/nednella/echo/commit/a9184068fa474665e6710aa53bcfe7c8d19ecaa6))


### Bug Fixes

* **api:** add missing protocol prefix to default CORS_ALLOWED_ORIGIN ([#111](https://github.com/nednella/echo/issues/111)) ([b158386](https://github.com/nednella/echo/commit/b158386e2d0461943c8c3f93a7a622f8c20b3f98))

## [0.2.0](https://github.com/nednella/echo/compare/api@v0.1.0...api@v0.2.0) (2025-09-24)


### Features

* **api:** add Spring actuator ([#104](https://github.com/nednella/echo/issues/104)) ([18ff892](https://github.com/nednella/echo/commit/18ff8921ccfc5dc32dd9106db73b1f00002bad99))
* **api:** update base URL ([#106](https://github.com/nednella/echo/issues/106)) ([1247c13](https://github.com/nednella/echo/commit/1247c13fa4c33057dd8c2010323f55c1978425d7))

## 0.1.0 (2025-09-20)


### Features

* monorepo setup ([#55](https://github.com/nednella/echo/issues/55)) ([46d6f85](https://github.com/nednella/echo/commit/46d6f858b8fccd9b24b6d220845ab5fd3ab0228b))


### Documentation

* move application-specific docs to the app root & update README.md ([#81](https://github.com/nednella/echo/issues/81)) ([3c3ed70](https://github.com/nednella/echo/commit/3c3ed70ab630099278793424e0504564a009d547))
