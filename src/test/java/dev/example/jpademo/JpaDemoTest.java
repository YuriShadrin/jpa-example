package dev.example.jpademo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import dev.example.jpademo.dto.CustomerDto;
import dev.example.jpademo.dto.EmployeeDto;
import dev.example.jpademo.dto.ProjectDto;
import dev.example.jpademo.exception.ItemNotFoundException;
import dev.example.jpademo.exception.ProjectNotEmptyException;
import dev.example.jpademo.services.CustomerService;
import dev.example.jpademo.services.EmployeeService;
import dev.example.jpademo.services.ProjectService;

@SpringBootTest(properties = { "spring.config.name=test-config" }, classes = { App.class })
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaDemoTest {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ProjectService projectService;

	private CustomerDto custMC, custOC;
	private EmployeeDto emplBG, emplLE, emplBM, emplSP;
	private ProjectDto projW, projO, projJ;

	@BeforeAll
	void setup() throws IOException {

		// customers
		custMC = customerService.createCustomer("Microsoft", """
				One Microsoft Way, \
				Redmond, WA 98052-6399, \
				USA""", "www.microsoft.com");

		custOC = customerService.createCustomer("Oracle", "100 Oracle Pkwy, Redwood City, CA 94065, USA",
				"www.oracle.com");

		// employees
		emplBG = employeeService.createEmployee("Bill", "Gates", (new GregorianCalendar(1955, 10, 28).getTime()));
		employeeService.setEmployeePhoto(emplBG.id(), loadResource("img/Bill_Gates.png"));

		emplLE = employeeService.createEmployee("Lawrence", "Ellison", (new GregorianCalendar(1944, 8, 17).getTime()));
		employeeService.setEmployeePhoto(emplLE.id(), loadResource("img/Larry_Ellison.png"));

		emplBM = employeeService.createEmployee("Bob", "Miner", (new GregorianCalendar(1941, 12, 23).getTime()));
		employeeService.setEmployeePhoto(emplBM.id(), loadResource("img/Robert_Miner.png"));

		emplSP = employeeService.createEmployee("Saravanan", "Paramasivam",
				(new GregorianCalendar(1971, 11, 3).getTime()));
		employeeService.setEmployeePhoto(emplSP.id(), loadResource("img/QA.png"));

		// projects
		projW = projectService.createProject("Windows", "Windows OS", custMC);
		projO = projectService.createProject("Oracle", "Oracle DB", custOC);
		projJ = projectService.createProject("JAVA", "JDK Development Kit", custOC);

		// assignments:
		// Bill Gates -> Windows
		employeeService.assignToProject(emplBG, projW);

		// Larry Ellison -> Oracle DB, JAVA
		employeeService.assignToProject(emplLE, projO);
		employeeService.assignToProject(emplLE, projJ);

		// Bob Miner -> Oracle DB
		employeeService.assignToProject(emplBM, projO);

		// QA -> Windows, Oracle DB, JAVA
		employeeService.assignToProject(emplSP, projW);
		employeeService.assignToProject(emplSP, projO);
		employeeService.assignToProject(emplSP, projJ);
	}

	@AfterAll
	void clean() {

	}

	@Test
	@Order(1)
	@DisplayName("Customer service tests")
	void customerServiceTests() {
		// valid customer
		assertThat(custMC).isEqualTo(customerService.getCustomer(custMC.id()));
		// invalid customer
		assertThrows(ItemNotFoundException.class, () -> customerService.getCustomer(-1L));

		// valid customer
		assertThat(custOC).isEqualTo(customerService.findCustomer(custOC.id()).orElseThrow());
		// invalid customer
		assertTrue(customerService.findCustomer(-1L).isEmpty());

		// find: all customers: name is "", page size 2
		assertThat(customerService.findCustomers("", PageRequest.ofSize(2)).size()).isEqualTo(2);
		// find: Oracle
		assertThat(customerService.findCustomers("o", PageRequest.ofSize(2)).size()).isEqualTo(2);
		// find: Microsoft
		assertThat(customerService.findCustomers("mi", PageRequest.ofSize(2)).size()).isEqualTo(1);
		// find: one customer per page
		assertThat(customerService.findCustomers(null, PageRequest.ofSize(1)).size()).isEqualTo(1);

		// update
		var cu1 = customerService.updateCustomer(custMC.id(), "NO ADDRESS", null);
		assertThat(cu1.address()).isEqualTo("NO ADDRESS");
		assertThat(cu1.siteUrl()).isEqualTo(custMC.siteUrl());

		var cu2 = customerService.updateCustomer(custMC.id(), null, "NO URL");
		assertThat(cu2.address()).isEqualTo(cu1.address());
		assertThat(cu2.siteUrl()).isEqualTo("NO URL");

		var cu3 = customerService.updateCustomer(custMC.id(), custMC.address(), custMC.siteUrl());
		assertThat(custMC).isEqualTo(cu3);
	}

	@Test
	@Order(2)
	@DisplayName("Project service tests")
	void projectServiceTests() {
		// valid project
		assertThat(projW).isEqualTo(projectService.getProject(projW.id()));
		// invalid project
		assertThrows(ItemNotFoundException.class, () -> projectService.getProject(-1L));

		// valid project
		assertThat(projJ).isEqualTo(projectService.findProject(projJ.id()).orElseThrow());
		// invalid project
		assertTrue(projectService.findProject(-1L).isEmpty());

		// update
		var pu1 = projectService.updateProject(projO.id(), "NO NAME", null);
		assertThat(pu1.name()).isEqualTo("NO NAME");
		assertThat(pu1.description()).isEqualTo(projO.description());

		var pu2 = projectService.updateProject(projO.id(), null, "NO DESCR");
		assertThat(pu2.name()).isEqualTo(pu1.name());
		assertThat(pu2.description()).isEqualTo("NO DESCR");

		var pu3 = projectService.updateProject(projO.id(), projO.name(), projO.description());
		assertThat(pu3).isEqualTo(projO);

		// find
		assertThat(projectService.findProjects(null, false).size()).isEqualTo(3);
		assertThat(projectService.findProjects("A", false).size()).isEqualTo(2);

		assertThat(projectService.getCustomerProjects(custMC.id()).size()).isEqualTo(1);
		assertThat(projectService.getCustomerProjects(custOC.id()).size()).isEqualTo(2);

		assertThat(projectService.getEmployeeProjects(emplBG.id()).size()).isEqualTo(1);
		assertThat(projectService.getEmployeeProjects(emplLE.id()).size()).isEqualTo(2);
		assertThat(projectService.getEmployeeProjects(emplBM.id()).size()).isEqualTo(1);
		assertThat(projectService.getEmployeeProjects(emplSP.id()).size()).isEqualTo(3);

		// delete
		assertThrows(ItemNotFoundException.class, () -> projectService.deleteProject(-1L));
		assertThrows(ProjectNotEmptyException.class, () -> projectService.deleteProject(projW.id()));
		projectService.getProject(projJ.id()).employees().forEach(e -> employeeService.deassignFromProject(e, projJ));
		projectService.deleteProject(projJ.id());
		assertThat(projectService.findProjects(null, false).size()).isEqualTo(2);
	}

	@Test
	@Order(3)
	@DisplayName("Employee service tests")
	void employeeServiceTests() {

		// valid employee
		assertThat(emplBG).isEqualTo(employeeService.getEmployee(emplBG.id()));
		// invalid employee
		assertThrows(ItemNotFoundException.class, () -> employeeService.getEmployee(-1L));

		// valid employee
		assertThat(emplBG).isEqualTo(employeeService.findEmployee(emplBG.id()).orElseThrow());
		// invalid employee
		assertTrue(employeeService.findEmployee(-1L).isEmpty());

		var emp1 = employeeService.updateEmployee(emplSP.id(), "NO NAME");
		assertThat(emp1.firstName()).isEqualTo(emplSP.firstName());
		assertThat(emp1.lastName()).isEqualTo("NO NAME");

		assertThrows(ItemNotFoundException.class, () -> employeeService.updateEmployee(-1L, "ANY STRING"));

		assertThat(employeeService.getProjectEmployees(projW.id()).size()).isEqualTo(2);
		assertThat(employeeService.getProjectEmployees(projJ.id()).size()).isEqualTo(0); // project was deleted in the
																							// prev test
		assertThat(employeeService.getProjectEmployees(projO.id()).size()).isEqualTo(3);

		assertThat(employeeService.getCustomerEmployees(custMC.id()).size()).isEqualTo(2);
		assertThat(employeeService.getCustomerEmployees(custOC.id()).size()).isEqualTo(3);

		employeeService.deleteEmployee(emplSP.id());
		assertTrue(employeeService.findEmployee(emplSP.id()).isEmpty());
		
		assertThat(employeeService.findEmployees("", PageRequest.ofSize(3)).size()).isEqualTo(3);
		assertThrows(NullPointerException.class, () -> employeeService.findEmployees(null, PageRequest.ofSize(3)));

		assertTrue(employeeService.getEmployeePhoto(-1L).isEmpty());
		assertTrue(employeeService.getEmployeePhoto(emplBG.id()).isPresent());
	}

	static byte[] loadResource(String resourceName) throws IOException {
		return JpaDemoTest.class.getClassLoader().getResourceAsStream(resourceName).readAllBytes();
	}
}
