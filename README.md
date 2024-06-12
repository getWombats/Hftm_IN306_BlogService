<a name="readme-top"></a>

![Quarkus](readme-images/quarkus_project_banner.png)

# Blog REST Service Project

Simple REST Service application.

This project is intended to implement the contents of the course IN306 "Verteilte Systeme".


## Prerequisites

#### Following extensions are needed in VS Code

*  [VS Code Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

*  [VS Code Quarkus Extension](https://marketplace.visualstudio.com/items?itemName=redhat.vscode-quarkus)

#### Additional tools

*  [Docker](https://www.docker.com/products/docker-desktop/) Because nothing runs without Docker :)

#### HTTP Client CLI or GUI like HTTPie, Postman or Bruno

* [HTTPie Desktop](https://httpie.io/download) / [HTTPie CLI](https://httpie.io/docs/cli/installation)

* [Postman](https://www.postman.com/) / [Postman CLI](https://learning.postman.com/docs/postman-cli/postman-cli-installation/)

* [Bruno Desktop](https://www.usebruno.com/downloads) / [Bruno CLI](https://docs.usebruno.com/bru-cli/overview)

## Run the App

1. Start Docker

2. Just type following command in your VS Code Terminal:

```sh
./mnvw quarkus:dev
```

## Run Quarkus Tests

Run all tests:
```sh
./mvnw test
```

Clean build and run all tests:
```sh
./mvnw clean test
```

## API Documentation

> Default Content-Type: application/json

When the project is running, the documentation can be seen via the [Swagger UI](http://localhost:8080/q/swagger-ui/)

## Making requests

#### Using HTTPie CLI

Get all blogs:
```sh
http -v GET :8080/blogs
```

Get filtered blogs:
```sh
http -v GET :8080/blogs searchString=="{your search string}"
```

Get blog by id:
```sh
http -v GET :8080/blogs/{id}
```

Add a blog:
```sh
http -v POST :8080/blogs title="Your Blogtitle" content="Your Blogcontent"
```

Delete a blog (passing id via header):
```sh
http -v DELETE :8080/blogs id:{id}
```

Replace a blog:
```sh
http -v PUT :8080/blogs/{id} title="Your new Blogtitle" content="Your new Blogcontent"
```

Update a blog (passing id via header):
```sh
http -v PATCH :8080/blogs id:6 title="Your updated Blogtitle" content="Your updated Blogcontent"
```
___

## Change Log

* [x] Initial commit with demo code
* [x] Updated readme
* [x] Added Lombok support
* [x] Added DependencyInjection support
* [x] Added Hibernate ORM with Panache Qurakus Extension for DB Connectivity and easier DB handling
* [x] Added JDBC Driver for MySQL Quarkus Extension
* [x] Implemented GET, POST, PUT, PATCH and DELETE Requests
* [x] Changed PATCH and DELETE request passing the blog id by request header instead of path

## Roadmap

* [ ] Put all opening curly braces on new line
* [ ] Some tests still fail, fix coming soon
* [ ] Complete unit tests