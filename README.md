# UserManagement
Spring boot REST service for basic user CRUD

## Installation

1. Clone
2. (Optional; the app can also be run standalone) Install [MongoDB](http://docs.mongodb.org/master/installation/) and fire up the Mongo daemon: `mongod`

## Build then fire up the app

1. `./gradlew clean build`
2. ` ./gradlew bootRun` for using the default profile for the in-memory repository, or
2a. `SPRING_PROFILES_ACTIVE=mongodb ./gradlew bootRun` for using the mongodb repository
3. Use curl commands to access endpoints; see curl-tests.txt

## Notes
This stuff is still pretty raw ,e.g. flat packaging and few integration tests, so is still a work in progress.
