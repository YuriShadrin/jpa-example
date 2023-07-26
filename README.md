# JPA EXAMPLE
The project is mainly intended for educational purposes. Nevertheless you can download it and modify as you need.
The project demonstrates how to create and use Java JPA persistent layer.

***Keep in mind that the project is not meant to be used in your production because it is not optimized for SQL queries.***

## Implementation features
* DTO layer is implemented using `Java records` (to be immutable)
* JPA Entities are isolated on the service layer
* JPA Repositories contain additional queries based on the Spring Framework JPA naming convention and using `@Query` annotation.
* Database (H2) is stored on disk, database schema is created by `Flyway` tool (Spring framework implicitly supports Flyeway, just added the appropriate dependency to pom.xml).

## Implementation details
### Entities
![Entities](https://lucid.app/publicSegments/view/31281517-d24f-4094-8b28-39648948f865/image.png)
### Relationships
* `Customer` has many `Project` (one-to-many association)
* `Project` has many `Employee` assigned, `Employee` partitipates in many `Project` (many-to-many association)
* `Employee` has a `EmployeePhoto` (one to one)

### Database schema
```sql
CREATE TABLE CUSTOMER (
	id		BIGINT NOT NULL AUTO_INCREMENT,
	name		VARCHAR(32) NOT NULL,
	address		VARCHAR(255) NOT NULL,
	site_url	VARCHAR(64),
	PRIMARY KEY (id)
);

CREATE TABLE PROJECT (
	id	    	BIGINT NOT NULL AUTO_INCREMENT,
	name		VARCHAR(32) NOT NULL,
	description	VARCHAR(255) NOT NULL,
	customer_id	BIGINT,
	PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER(id)
);

CREATE TABLE EMPLOYEE (
	id		BIGINT NOT NULL AUTO_INCREMENT,
	first_name	VARCHAR(32) NOT NULL,
	last_name	VARCHAR(32) NOT NULL,
	birth_date	DATE,
	photo_id	BIGINT,
	PRIMARY KEY (id)
);

CREATE TABLE EMPLOYEE_PHOTO (
	id	    	BIGINT NOT NULL,
	photo		BLOB,
	PRIMARY KEY (id)
);

CREATE TABLE PROJECT_EMPLOYEE (
	project_id  	BIGINT NOT NULL,
	employee_id 	BIGINT NOT NULL,
	PRIMARY KEY (project_id, employee_id)
);
```
### One-to-Many association
```java
@Entity
@Table(name = "CUSTOMER")
public class Customer {
    ...
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "customer_id")
	private List<Project> projects;
    ...
}

@Entity
@Table(name = "PROJECT")
public class Project {
    ...
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
    ...
}
```
Note: `FetchType.EAGER` is used here just as an example.
### Many-to-Many association
```java
@Entity
@Table(name = "PROJECT")
public class Project {
    ...
	@ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	private List<Employee> employees = new ArrayList<>();
    ...
}
@Entity
@Table(name = "EMPLOYEE")
public class Employee {
    ...
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinTable(name = "project_employee", 
		joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"))
	private List<Project> projects = new ArrayList<>();
    ...
}
```
Note: `FetchType.LAZY` is used here just as an example. Be aware that all LAZY relations are resolved inside one session to avoid exception like *Hibernate could not initialize proxy – no Session*.
## Run tests 
```
mvn clean test
```
Note: clean also removes the saved database. This is necessary for the correct completion of the tests. You also can connect to the stored database any H2 client to view the data inside after all tests are completed.

------------------
Take and try!

