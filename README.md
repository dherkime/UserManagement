# UserManagement
Spring boot REST service for basic user CRUD

## Installation

1. Clone
2. (Optional; the app can also be run standalone) Install [MongoDB](http://docs.mongodb.org/master/installation/) and fire up the Mongo daemon: `mongod`

## Fire up the app

1. `./gradlew clean build && java -jar build/libs/user-management-0.1.0.jar`
2. Use curl commands to access endpoints; see curl-tests.txt

## Notes
This stuff is still pretty raw, e.g. flat packaging, few integration tests, HATEOAS linking, and MongoDB work still in progress.
