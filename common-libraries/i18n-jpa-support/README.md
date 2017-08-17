# I18n JPA Support

This library can be used to enhance entities or objects to have i18n support that are dynamically looked up from a `I18nMessageRepository` JPA repository.

Steps to utilize this library:
1. [Add `i18n-jpa-support` Dependency to the Project](#1-add-i18n-jpa-support-dependency-to-the-project)
2. [Add/Configure Annotations in Spring Boot Application Starter Class](#2-addconfigure-annotations-in-spring-boot-application-starter-class)
3. [Create `i18n_message` Table](#3-create-i18n_message-table)
4. [Enhance the Entities/Classes to Add I18n Support](#4-enhance-the-entitiesclasses-to-add-i18n-support)
5. [Insert Messages to the Database](#5-insert-messages-to-the-database)
6. [Use `I18nService` to Get Messages](#6-use-i18nservice-to-get-messages)

## Prerequisites

This library expects the dependencies that are listed below to be available in the project that is used in. These dependencies will be transitively available in a typical Spring Boot application with `org.springframework.boot:spring-boot-starter-data-jpa` dependency.

+ `org.springframework:spring-tx`
+ `org.springframework:spring-context`
+ `org.springframework.boot:spring-boot-autoconfigure`
+ `org.springframework.data:spring-data-jpa`
+ `org.hibernate.javax.persistence:hibernate-jpa-2.1-api`
+ `org.hibernate:hibernate-validator`

## Configuration

### 1. Add `i18n-jpa-support` Dependency to the Project

```xml
        <dependency>
            <groupId>gov.samhsa.c2s</groupId>
            <artifactId>i18n-jpa-support</artifactId>
            <version>${c2s.common-libraries.version}</version>
        </dependency>
```

### 2. Add/Configure Annotations in Spring Boot Application Starter Class

+ Add `@EnableI18nJpaSupport` annotation
+ Add/Modify `@EnableJpaRepositories` to also scan the package of `gov.samhsa.c2s.common.i18n.I18nEnabled`

```java
import gov.samhsa.c2s.common.i18n.config.EnableI18nJpaSupport;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
...
@SpringBootApplication
...
@EnableI18nJpaSupport
@EnableJpaRepositories(basePackageClasses = {MyApplication.class, I18nEnabled.class})
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

**NOTE: `MyApplication.class` is also added to `@EnableJpaRepositories.basePackageClasses`, so Sprig Boot can also scan the repositories in the `MyApplication` class base package in addition to the `I18nEnabled`.**

### 3. Create `i18n_message` Table

[create_i18n_message.sql](src/main/resources/create_i18n_message.sql)

```sql
create table i18n_message(
    id bigint not null,
    `key` varchar(255) not null,
    description varchar(255) not null,
    locale   varchar(255) not null,
    message varchar(20000) not null
) ENGINE=InnoDB;

alter table i18n_message
  add constraint pk_i18n_message primary key (id, `key`, locale);

alter table i18n_message
  add constraint u_i18n_message unique (`key`, locale);
```

### 4. Enhance the Entities/Classes to Add I18n Support

+ Implement `gov.samhsa.c2s.common.i18n.I18nEnabled` interface in the entity/class that you want to enhance
	+ **NOTE: Just implement the `gov.samhsa.c2s.common.i18n.I18nEnabled.getIdAsString()` method from this interface and consider utilizing the `default gov.samhsa.c2s.common.i18n.I18nEnabled.longToString(Long id): String` method if you just need to convert an `id` of `Long` type to `String`.**

```java
import gov.samhsa.c2s.common.i18n.I18nEnabled;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class MyEntity implements I18nEnabled {
    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @Override
    public String getIdAsString() {
        return longToString(id);
    }
}
```

### 5. Insert Messages to the Database

The convention for the message key is `SIMPLE_CLASS_NAME.ID_AS_STRING.FIELD_NAME`. When split by `.` as the delimiter, the first and last segments are supposed to be in `UPPER_CAMEL_CASE` format.

```sql
insert into i18n_message values (1, 'MY_ENTITY.1.DISPLAY','Description for the key','en', 'My description');
insert into i18n_message values (2, 'MY_ENTITY.1.DISPLAY','Descripción de la clave','es', 'Mi descripción');
```

### 6. Use `I18nService` to Get Messages

```java
public interface I18nService {

    @Transactional(readOnly = true)
    String getI18nMessage(I18nEnabled entityReference, String fieldName, Supplier<String> defaultMessageSupplier);
}
```

```java
import gov.samhsa.c2s.common.i18n.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
...
public class MyService {
    @Autowired
    private I18nService i18nService;

    public String getMessage(MyEntity myEntity){
        return i18nService.getI18nMessage(myEntity, "description", myEntity::getDescription)
   }
}
```

**NOTE: `I18nService` uses `org.springframework.context.i18n.LocaleContextHolder` to get the `Locale` from the current request context. If `Accept-Language` HTTP Header is passed to the `RestController` when making the request, Spring will automatically configure `LocaleContextHolder`.**

## Future

It is planned to improve this library to use AOP (potentially AspectJ) on entity/object fields/methods, so an explicit use of `I18nService` won't be required and the i18n contents can be returned transparently.

Example:

```java
@Entity
@Data
public class MyEntity {
    @Id
    @GeneratedValue
    private Long id;
	
    // at field if possible, but it is more likely to be at the getter method
	@I18nEnabled
    private String description;

	// at getters, I18nService can be called from this pointcut to lookup 
    // i18n messages from the database before returning the this.description as default value
    @I18nEnabled
    public String getDescription(){
        return this.description;
    }
}
```