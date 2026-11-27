# TIS Audit User Access

## About

A service for holding and providing aggregated information about users' accessing TIS data.

## Technology

- Java 25
- Spring Boot
- Gradle
- JUnit 5

Boilerplate code is generated with Lombok and MapStruct.

Code quality checking and enforcement is done with the following tools:

- EditorConfig
- Checkstyle
- JaCoCo
- SonarQube

## Security

Service clients submit requests with an `Authorization: Bearer <access-token>`.
Tokens must be signed JWTs issued by `OAUTH2_ISSUER_URI` and include `OAUTH2_AUDIENCE`

Requests without a valid token receive `401 Unauthorized`; valid tokens without the
required scope receive `403 Forbidden`. All other endpoints, including Actuator, require
an authenticated request.

## Local development

Run the application with `./gradlew bootRun` and run the test suite with
`./gradlew test`. The service listens on port `8098` with context path
`/useraccess`.

## License

This project is licensed under the [MIT License](LICENSE).
