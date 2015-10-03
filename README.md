# UserManagement
Spring boot REST service for basic user CRUD

## Installation

1. Clone
2. (Optional; the app can also be run standalone) Install [MongoDB](http://docs.mongodb.org/master/installation/) and fire up the Mongo daemon: `mongod`

## Build then fire up the app

1. `./gradlew clean build`
2. ` ./gradlew bootRun` for using the default profile for the in-memory repository, or `SPRING_PROFILES_ACTIVE=mongodb ./gradlew bootRun` for using the mongodb repository
3. Use curl commands to access endpoints; see curl-tests.txt

## Swagger
I used [SpringFox](https://github.com/springfox/springfox) annotations to generate [Swagger](http://swagger.io/) REST API documentation.

I also cloned, `npm install`ed, modified the dist/index.html, then ran `gulp serve` to fire up the swagger-ui web app.

The modification of the index.html was as follows:
```
if (url && url.length > 1) {
  url = decodeURIComponent(url[1]);
} else {
  url = "http://localhost:9000/v2/api-docs?group=user-management-api";
}
```

Once gulp is serving, access the Swagger API docs by going to [http://localhost:8080/](http://localhost:8080/)
