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

## Local development

Run the application with `./gradlew bootRun` and run the test suite with
`./gradlew test`. The service listens on port `8098` with context path
`/useraccess`.

## License

This project is licensed under the [MIT License](LICENSE).
