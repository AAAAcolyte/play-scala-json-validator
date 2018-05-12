# JSON Validation service

This is my first time to do a Scala project - Almost all techniques involved are not familiar to me,
and due to the time constraint and study pressure this project may be flawed.

# How to use

Download the project open terminal in directory /play-scala-jason-validator
execute /sbt run, the project will need the use of port 9000, it doesn't have a GUI,
users are supposed to interact using RESTful APIs.

It supports \
POST    /schema/:SCHEMAID \
GET     /schema/:SCHEMAID \
POST    /validate/SCHEMAID

Example using Curl: \
curl http://localhost:9000/schema -X POST -d @yourjsonschema.json -H "Content-Type: Application/Json" \
curl http://localhost:9000/validate -X POST -d @yourjsontotest.json -H "Content-Type: Application/Json"