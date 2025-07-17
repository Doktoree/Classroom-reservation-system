package doktoree.backend.repository;

import doktoree.backend.domain.Department;
import doktoree.backend.domain.Employee;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.Title;
import doktoree.backend.repositories.DepartmentRepository;
import doktoree.backend.repositories.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Employee employee;

    private Employee employee2;

    private Department department;

    @BeforeEach
    public void setup(){

        department = new Department();
        department.setShortName("dep1");
        department.setName("Department 1");

        employee = new Employee();
        employee.setLastName("Last name");
        employee.setName("Name");
        employee.setTitle(Title.MD);
        employee.setAcademicRank(AcademicRank.FULL_PROFESSOR);
        employee.setDepartment(department);

        employee2 = new Employee();
        employee2.setLastName("Last name1");
        employee2.setName("Name1");
        employee2.setTitle(Title.JD);
        employee2.setAcademicRank(AcademicRank.FULL_PROFESSOR);
        employee2.setDepartment(department);
    }

    @Test
    public void findAll_returnsPage(){

        departmentRepository.save(department);
        employeeRepository.saveAll(List.of(employee,employee2));

        Page<Employee> page = employeeRepository.findAll(PageRequest.of(0,10));
        List<Employee> list = page.getContent();
        Assertions.assertThat(list.size()).isEqualTo(2);


    }

}
