# Very Basic backend for Profile Management

This backend expose a REST API to create/update/delete operator profile
stored in a relational database.

This application is based on springboot REST to expose an API
and on springboot JPA to access the PostgreSQL database.
It is possible to test this module using an H2 in memory database.
The JDBC connection is defined in the application.properties file using springboot configuration.
 
The list of REST API is accessible using a SWAGGER endpoint
The default endpoint for swagger: http://localhost:8080/swagger-ui.html

The database schema for PostgresSQL is defined in schema.sql

A sample script init_data.sh show how to create data using curl.