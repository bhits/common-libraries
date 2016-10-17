# Common Libraries

The Common Libraries is a collection of useful libraries that are used by the Consent2Share (C2S) APIs. The Common Libraries contains a parent `pom.xml` file that aggregates the following libraries:

+ audit-service: Wrapper library for Logback Audit client.
+ common-unit: Utility library for unit testing. Contains utilities for reading resources, comparing and asserting XMLs, and `ArgumentMatcher` factory method using `java.util.function.Predicate` to support lambda expressions.
+ common-util: Utility library which contains the tools to encrypt, manage Strings, and generate unique values. 
+ consent-gen: Library to generate XACML from a `ConsentDto` object model.
+ document-accessor: Provides methods to access `org.w3c.dom.Document` typed XML document nodes.
+ document-converter: Converts an XML document from `org.w3c.dom.Document` type to `String` and vice versa.
+ document-transformer: Utility library to perform XML document transforms.
+ file-reader: Utility library to read files.
+ logger-wrapper: An [SLF4J](http://www.slf4j.org/) (Simple Logging Facade for Java) logger wrapper to add support for [Supplier functional interface](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html) arguments for lazy computation while logging.
+ marshaller: Responsible for marshalling and unmarshalling XML.
+ namespace: An implementation of `javax.xml.namespace.NamespaceContext` that can be used as the default implementation for commonly used namespaces and URIs in C2S.
+ oauth2: Contains utilities for building OAuth2 security expressions.
+ param: Library to manage key-value parameter pairs.
+ resource-url: Library to compose the resource URL of a given filename within a given package.
+ validator-extension: Extension library to `hibernate-validator` to add support for using `@Past` and `@Future` with `java.time.LocalDate`.
+ xdm (eXtreme Download Manager): Provides zip utilities.
+ xml-validation: Provides utilities to validate XML files against a schema.

## Build

### Prerequisites

+ [Oracle Java JDK 8 with Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### Commands

This is a Maven project and requires [Apache Maven](https://maven.apache.org/) 3.3.3 or greater to build it. It is recommended to use the *Maven Wrapper* scripts provided with this project. *Maven Wrapper* requires an internet connection to download Maven and project dependencies for the very first build.

To build the project, navigate to the folder that contains the parent `pom.xml` file using terminal/command line.

+ To build a JAR:
    + For Windows, run `mvnw.cmd clean install`
    + For *nix systems, run `mvnw clean install`

**NOTE: The common libraries need to be built and installed to the local Maven repository or deployed to the Maven repository used in your enterprise development environment before building any other C2S microservices, in order to prevent any dependency resolution issues.**

## Versioning

The versioning convention for common libraries:

 + Releases: `<MajorVersion>.<MinorVersion>.<IncrementalVersion>`
 + Snapshots: `<MajorVersion>.<MinorVersion>.<IncrementalVersion>-SNAPSHOT`

## Manual Release Process

1. Pick the commit to cut the release
2. Create a temporary branch for the release from the selected commit
3. Checkout the temporary branch
4. Set the version to release (ie: if it was `1.0.3-SNAPSHOT`, move it to `1.0.3`)
	+ *HINT: `mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$new_version`*
5. Commit the release version on the temporary branch. The commit message should be something like `Release version 1.0.3`
6. Tag this final release commit with the version number (ie: `1.0.3`)
7. Delete the temporary branch
8. Push the tag
9. Checkout base branch (ie: master) and move the version to the next development version (ie: `1.0.4-SNAPSHOT`)

## Branching

A new branch can be created right before a minor or major version upgrade, so the older versions can continue to have incremental versions for improvement and mostly hot fixes as necessary.

Example: Given the current development version `1.0.3-SNAPSHOT` and previous releases `1.0.0`, `1.0.1`, `1.0.2`, if it is decided to move the development to next minor version `1.1.0-SNAPSHOT`:
+ Create a branch named `1.0.x` from master
+ Move master to `1.1.0-SNAPSHOT` and commit
+ Push master and the new branch `1.0.x`

If there is any development required in an older version, all the new commits must be gradually merged to the branches newer than that version up until the master branch.

Example: If a hot fix is required in an older `1.2.x` branch with the final released version `1.2.4` and current development version `1.2.5-SNAPSHOT`:

 1. Fix the bug on `1.2.x` branch
 2. Release version `1.2.5` by following the manual release process
 3. Merge `1.2.x` to `1.3.x`, merge `1.3.x` to `1.4.x` ...etc
 5. Finally merge `1.4.x` to `master`

 **NOTE: It is highly recommended to use the most recent releases of the most recent versions in the client projects.** 

## Contact

If you have any questions, comments, or concerns please see the [Consent2Share]() project site.

## Report Issues

Please use [GitHub Issues](https://github.com/bhits/phr-api/issues) page to report issues.

[//]: # (License)
