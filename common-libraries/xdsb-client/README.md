## XDS.b Client

This is a library that provides  SOAP clients to connect to a server that implements Integrating the Healthcare Enterprise (IHE). This is tested with [HIEOS](https://github.com/kef/hieos) which is an implementation. In Fei Systems, an internal deployment is available at this [location](http://bhitsdevhie01:9080/axis2/services/listServices). 

## Implementation

It implements three XDS transactions:

+ Document Repository

	+ Provide and Register Document Set-b [ITI-41] : A document source initiates this transaction. For each document submitted, the source provides both the documents as an opaque octet stream and the corresponding metadata to the Document Repository.

	+ Retrieve Document Set [ITI-43] : A document consumer initiates this transaction. The Document Repository shall return the document set that was specified by the document consumer.

+ Document Registry

	+ Registry Stored Query [ITI-18] : This transaction is issued by the document consumer on behalf of a care provider to Document Registry. The Document Registry searches the registry to locate documents that meet the provider's specified query criteria. It will return registry metadata and identifier of each document in one or more Document Repositories.

The XDS.b profile provides Web Services Definition Language (WSDL) contracts for all the required IHE XDS.b Transactions. The WSLs used in this project can be found [here](ftp://ftp.ihe.net/TF_Implementation_Material/ITI/wsdl/).

## Prerequisites

+ Oracle Java JDK 8
+ Apache Maven


## Build

This project requires [Apache Maven](https://maven.apache.org) to build it. To build the project, navigate to the folder that contains `pom.xml` file using the terminal/command line.

+ To build a JAR:
    + Run `mvn clean package`


## Add as a dependency

This is a standard maven project. This project can be added in another maven project as a dependency.

```yml
<dependency>
  <groupId>gov.samhsa.c2s</groupId>
  <artifactId>common-libraries</artifactId>
  <version>1.14.0-SNAPSHOT</version>
</dependency>
```


## References
+ [IHE IT Infrastructure Technical Framework Supplement](http://www.ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol1.pdf)