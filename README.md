# Branch Manager Address Book API

Simple API service for managing contacts within a collection of address books.

---

As a Branch Manager,\
I would like an address book application\
So that I can keep track of my customer contacts.

## Acceptance Criteria
- [x] Address book will hold name and phone numbers of contact entries
- [x] Create a REST API which will have endpoints for the following:
    - [x] Users should be able to add new contact entries
    - [x] Users should be able to remove existing contact entries
    - [x] Users should be able to print all contacts in an address book
    - [x] Users should be able to maintain multiple address books
    - [x] Users should be able to print a unique set of all contacts across multiple address books
    
An API spec would be nice to have.
All acceptance criteria should be covered by unit tests, with additional integration tests where appropriate.
Tests will be included in the assessment criteria, as will demonstration of other good coding practices like - clear separation of concerns, clean code, standardised formatting, etc.
Data can be persisted in a storage medium of your choice - like - In-mem DB, Files, etc.

Finally, containerise the application using docker so that it can be deployed on the Kubernetes Platform.
```bash
## Generate Docker image
gradle bootBuildImage --imageName=example/springboot-addressbook

## Run Docker Image 
docker run -p 8080:8080 -t example/springboot-addressbook:latest
```

__NOTE:__ Solution must be written in __Java + Spring Boot__
