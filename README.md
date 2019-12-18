# frdp-framework

ForgeRock Demonstration Platform Java framework.  Contains Java packages that are used by other projects.

## Requirements

The following items must be installed:

1. [Apache Maven](https://maven.apache.org/)
1. [Java Development Kit 8](https://openjdk.java.net/)

## Dependencies

The following Java packages are required.  The `mvn` process should automatically dowload and install the packages, see `pom.xml` for details:

- `javax.ws.rs`
  - `javax.ws.rs-api`
- `com.googlecode.json-simple`
  - `json-simple`
- `javax.servlet`
  - `servlet-api`

## Build

Run "maven" processes to clean, compile and install the packages:

`mvn clean`

`mvn compile`

`mvn install`

Packages are add to the user's home folder: 

`~/.m2/repository/com/forgerock/frdp/frdp-framework`

----
