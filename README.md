Common Libraries
=================================

Common libraries is a collection of useful libraries that are used by Consent2Share (C2S) APIs. It is made up of the following:

+ audit-service: The audit server client.
+ common-unit: Utility library for unit test. It has resource file reader, class to compare xml and etc.
+ common-util: Utility library which contains tools to encrypt, manage Strings, and generate unique value. 
+ consent-gen: Provides a feature to generates XACML from consent.
+ document-accessor: Provides methods to access an XML document.
+ document-converter: Converts an XML document to String and vice versa.
+ document-transformer: Utility library to perform transform XML document.
+ file-reader: Utility library to read file.
+ logger-wrapper: A [SLF4J](http://www.slf4j.org/) (Simple Logging Facade for Java) logger that wraps the [Supplier functional interface](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html).
+ marshaller: Responsible for marshalling and unmarshalling XML.
+ namespace: Sets the default name space and provides methods to get the prefixes and name space URI.
+ oauth2: Contains OAuth2 libraries to verify scopes and to manage OAuth2 tokens.
+ param: Library to manage a key-value parameter pairs. It has methods to set, get parameters, and so on.
+ resource-url: Compose resource URL. 
+ validator-extension: Library to validate date. For example past and future dates.
+ xdm(eXtreme Download Manager): Provides zip utilities.
+ xml-validation: Provides utility libraries to validate XML.


## Build

### Prerequisites

+ [Oracle Java JDK 8 with Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy](http://www.oracle.com/technetwork/java/javase/downloads/index.html)


### Commands

This is a Maven project and requires [Apache Maven](https://maven.apache.org/) 3.3.3 or greater to build it. It is recommended to use the *Maven Wrapper* scripts provided with this project. *Maven Wrapper* requires an internet connection to download Maven and project dependencies for the very first build.

To build the project, navigate to the folder that contains `pom.xml` file using terminal/command line.

+ To build a JAR:
    + For Windows, run `mvnw.cmd clean install`
    + For *nix systems, run `mvnw clean install`

## 1. Versioning

The versioning convention for common libraries:

 + Releases: `<MajorVersion>.<MinorVersion>.<IncrementalVersion>`
 + Snapshots: `<MajorVersion>.<MinorVersion>.<IncrementalVersion>-SNAPSHOT`

---

## 2. Manual Release Process

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

---

## 3. Branching

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
