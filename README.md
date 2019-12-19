# frdp-framework

ForgeRock Demonstration Platform Java framework.  Contains Java packages that are used by other projects.

## Requirements

The following items must be installed:

1. [Apache Maven](https://maven.apache.org/)
1. [Java Development Kit 8](https://openjdk.java.net/)

## Dependencies

The following Java packages are required.  The `mvn` process automatically dowloads and installs the packages, see `pom.xml` for details:

```
    <dependencies>
        <dependency>
            <groupId>javax.ws.rs</groupId>
            <artifactId>javax.ws.rs-api</artifactId>
            <version>2.0</version>
            <type>jar</type>
        </dependency>
        <dependency>
            <groupId>com.googlecode.json-simple</groupId>
            <artifactId>json-simple</artifactId>
            <version>1.1.1</version>
            <type>jar</type>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>servlet-api</artifactId>
            <version>2.3</version>
            <type>jar</type>
        </dependency>
    </dependencies>
```

## Build

Run "maven" processes to clean, compile pacakge and install:

`mvn clean compile package install`

Packages are added to the user's home folder: 

`~/.m2/repository/com/forgerock/frdp/frdp-framework`

----
