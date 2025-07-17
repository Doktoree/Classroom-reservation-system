package doktoree.backend.repository;

import doktoree.backend.domain.Department;
import doktoree.backend.domain.Employee;
import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.User;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.Role;
import doktoree.backend.enums.Title;
import doktoree.backend.repositories.DepartmentRepository;
import doktoree.backend.repositories.EmployeeRepository;
import doktoree.backend.repositories.UserRepository;
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
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private User user;

    private User user2;

    @BeforeEach
    public void setup(){

        Department department = new Department();
        department.setName("Department");
        department.setShortName("dep");
        Department savedDepartment = departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setTitle(Title.MD);
        employee.setAcademicRank(AcademicRank.FULL_PROFESSOR);
        employee.setLastName("Last");
        employee.setName("Name");
        employee.setDepartment(savedDepartment);
        Employee savedEmployee = employeeRepository.save(employee);


        user = new User();
        user.setRole(Role.USER);
        user.setPassword("pass");
        user.setEmail("mail@gmail.com");
        user.setEmployee(savedEmployee);
        user = userRepository.save(user);

        user2 = new User();
        user2.setRole(Role.ADMIN);
        user2.setPassword("pass2");
        user2.setEmail("mejl@gmail.com");
        user2.setEmployee(savedEmployee);
        user2 = userRepository.save(user2);

    }

    @Test
    public void whenFindAll_thenReturnPage(){

        userRepository.saveAll(List.of(user,user2));

        Page<User> page = userRepository.findAll(PageRequest.of(0,10));
        List<User> users = page.getContent();
        Assertions.assertThat(users.size()).isEqualTo(2);
        Assertions.assertThat(users.get(0).getId()).isEqualTo(user.getId());
        Assertions.assertThat(users.get(1).getId()).isEqualTo(user2.getId());
    }

    @Test
    public void whenFindByEmail_thenReturnOptionalUser(){

        userRepository.saveAll(List.of(user,user2));
        Optional<User> optionalUser = userRepository.findByEmail("mail@gmail.com");
        Assertions.assertThat(optionalUser.get().getId()).isEqualTo(user.getId());

    }

    @Test
    public void whenFindUserByEmployeeIdAndRole_thenReturnOptionalUser(){

        userRepository.saveAll(List.of(user,user2));
        Optional<User> optionalUser = userRepository.findUserByEmployeeIdAndRole(user.getEmployee().getId(), user.getRole());
        Assertions.assertThat(optionalUser.get().getId()).isEqualTo(user.getId());

    }

}
