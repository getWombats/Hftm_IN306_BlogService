![Quarkus](readme-images/quarkus_project_banner.png)

<h1 align="center">Blog REST Service Project</h1>

Simple REST Service application.

This project is intended to implement the contents of the course IN306 "Verteilte Systeme".

<br>
<br>

# Table of Contents

## [► Prerequisites](readme-pages/Prerequisites.md)

## [► Run the app manually](readme-pages/RunTheAppManually.md)

## [► Run the app with docker compose](readme-pages/RunTheAppDockerCompose.md)

## [► See the API Documentation](readme-pages/ApiDocumentation.md)

## [► Run unit and integration tests](readme-pages/Testing.md)

<br>
<br>

# Change Log

* [x] Initial commit with demo code
* [x] Updated readme
* [x] Added Lombok support
* [x] Added DependencyInjection support
* [x] Added Hibernate ORM with Panache Qurakus Extension for DB Connectivity and easier DB handling
* [x] Added JDBC Driver for MySQL Quarkus Extension
* [x] Implemented GET, POST, PATCH and DELETE Requests
* [x] Implemented open id connect authentication by jwt
* [x] Implemented unit and integration tests
* [x] Implemented tests with and without authorization
* [x] Implemented dev and prod profile
* [x] Implemented flyway database migration
* [x] Added github actions workflow yaml for automatically push image to github container registry
* [x] Added docker compose file to run production profile
* [x] Added github actions workflow yaml for automatically push **native** image to github container registry. Removed non-native workflow build

# Roadmap

* [ ] Put all opening curly braces on new line (a hard one)
* [ ] Pet my guinea pig. unfortunalety i dont have one.
* [ ] Implement user accounts corresponding to keycloak accounts OR connect keycloak accounts

<!------------------------------------------------------------------------------------------------------------------>