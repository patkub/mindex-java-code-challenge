# Coding Challenge
## What's Provided
A simple [Spring Boot](https://projects.spring.io/spring-boot/) web application has been created and bootstrapped 
with data. The application contains information about all employees at a company. On application start-up, an in-memory 
Mongo database is bootstrapped with a serialized snapshot of the database. While the application runs, the data may be
accessed and mutated in the database without impacting the snapshot.

### How to Run
The application may be executed by running `gradlew bootRun`.

On Linux, make sure to run the gradlew script that is in the current directory:
```
chmod +x gradlew
./gradlew bootRun
```

### How to Use
The following endpoints are available to use:
```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/employee
    * PAYLOAD: Employee
    * RESPONSE: Employee
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}
    * RESPONSE: Employee
* UPDATE
    * HTTP Method: PUT 
    * URL: localhost:8080/employee/{id}
    * PAYLOAD: Employee
    * RESPONSE: Employee
```
The Employee has a JSON schema of:
```json
{
  "type":"Employee",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "firstName": {
      "type": "string"
    },
    "lastName": {
          "type": "string"
    },
    "position": {
          "type": "string"
    },
    "department": {
          "type": "string"
    },
    "directReports": {
      "type": "array",
      "items" : "string"
    }
  }
}
```
For all endpoints that require an "id" in the URL, this is the "employeeId" field.

## What to Implement
Clone or download the repository, do not fork it.

### Task 1
Create a new type, ReportingStructure, that has two properties: employee and numberOfReports.

For the field "numberOfReports", this should equal the total number of reports under a given employee. The number of 
reports is determined to be the number of directReports for an employee and all of their distinct reports. For example, 
given the following employee structure:
```
                    John Lennon
                /               \
         Paul McCartney         Ringo Starr
                               /        \
                          Pete Best     George Harrison
```
The numberOfReports for employee John Lennon (employeeId: 16a596ae-edd3-4847-99fe-c4518e82c86f) would be equal to 4. 

This new type should have a new REST endpoint created for it. This new endpoint should accept an employeeId and return 
the fully filled out ReportingStructure for the specified employeeId. The values should be computed on the fly and will 
not be persisted.

### Task 2
Create a new type, Compensation. A Compensation has the following fields: employee, salary, and effectiveDate. Create 
two new Compensation REST endpoints. One to create and one to read by employeeId. These should persist and query the 
Compensation from the persistence layer.

## Delivery
Please upload your results to a publicly accessible Git repo. Free ones are provided by Github and Bitbucket.


## My Notes (Patrick Kubiak):
- Added instruction to run on Linux

Task 1:

Example:

http://localhost:8080/reports/16a596ae-edd3-4847-99fe-c4518e82c86f  
numberOfReports is 4
```
16a596ae-edd3-4847-99fe-c4518e82c86f
    - b7839309-3348-463b-a7e3-5de1c168beb3
    - 03aa1462-ffa9-4978-901b-7c001562cf6f
        - 62c1084e-6e34-4630-93fd-9153afb65309
        - c0c2293d-16bd-4603-8e08-638a9d18b22c
```

Task 2:

Example:

Initially, GET http://localhost:8080/compensation/03aa1462-ffa9-4978-901b-7c001562cf6f replies with an
InternalServerError because compensation for employeeId: 03aa1462-ffa9-4978-901b-7c001562cf6f does not exist.
```json
{
    "timestamp": "2021-11-05T05:50:15.310+0000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Invalid employeeId: 03aa1462-ffa9-4978-901b-7c001562cf6f",
    "path": "/compensation/03aa1462-ffa9-4978-901b-7c001562cf6f"
}
```

First, POST http://localhost:8080/compensation with json body, for example
```json
{
    "employeeId": "03aa1462-ffa9-4978-901b-7c001562cf6f",
    "salary": 28.99,
    "effectiveDate": "2021-11-04"
}
```

Then, GET http://localhost:8080/compensation/03aa1462-ffa9-4978-901b-7c001562cf6f replies with a formatted date:
```json
{
    "employeeId": "03aa1462-ffa9-4978-901b-7c001562cf6f",
    "salary": 28.99,
    "effectiveDate": "2021-11-04T00:00:00.000+0000"
}
```

Attempting to POST compensation for the same employeeId again will reply with an error.
```json
{
    "timestamp": "2021-11-05T05:53:19.621+0000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Compensation for employeeId: 03aa1462-ffa9-4978-901b-7c001562cf6f already exists!",
    "path": "/compensation"
}
```

### Existing code issues:

Direct reports display as an Employee json object. Instead, it should only be a list of employeeIds.
Looking at http://localhost:8080/employee/16a596ae-edd3-4847-99fe-c4518e82c86f
```json
{
    "employeeId": "16a596ae-edd3-4847-99fe-c4518e82c86f",
    "firstName": "John",
    "lastName": "Lennon",
    "position": "Development Manager",
    "department": "Engineering",
    "directReports": [
        { "employeeId": "b7839309-3348-463b-a7e3-5de1c168beb3", "firstName": null, "lastName": null, "position": null, "department": null, "directReports": null },
        { "employeeId": "03aa1462-ffa9-4978-901b-7c001562cf6f", "firstName": null, "lastName": null, "position": null, "department": null, "directReports": null }
    ]
}
```
Fixed this by creating a DirectReport class with just an employeeId property.
```json
{
    "employeeId": "16a596ae-edd3-4847-99fe-c4518e82c86f",
    "firstName": "John",
    "lastName": "Lennon",
    "position": "Development Manager",
    "department": "Engineering",
    "directReports": [
        {
            "employeeId": "b7839309-3348-463b-a7e3-5de1c168beb3"
        },
        {
            "employeeId": "03aa1462-ffa9-4978-901b-7c001562cf6f"
        }
    ]
}
```