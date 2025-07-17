package doktoree.backend.service;

import doktoree.backend.domain.Department;
import doktoree.backend.domain.Employee;
import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.Title;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.repositories.EmployeeRepository;
import doktoree.backend.services.EmployeeService;
import doktoree.backend.services.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    private Department department;

    @BeforeEach
    public void setup(){

        department = new Department();
        department.setId(2L);
        department.setName("Department 1");
        department.setShortName("DEP1");

        employee = new Employee();
        employee.setId(1L);
        employee.setName("Name 1");
        employee.setTitle(Title.PHD);
        employee.setDepartment(department);
        employee.setLastName("Last Name 1");
        employee.setAcademicRank(AcademicRank.ASSISTANT_PROFESSOR);

    }

    public void check(EmployeeDto dto, Employee employee){
        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo(employee.getName());
        assertThat(dto.getTitle()).isEqualTo(employee.getTitle());
        assertThat(dto.getId()).isEqualTo(employee.getId());
        assertThat(dto.getAcademicRank()).isEqualTo(employee.getAcademicRank());
        assertThat(dto.getLastName()).isEqualTo(employee.getLastName());
        assertThat(dto.getDepartmentId()).isEqualTo(employee.getDepartment().getId());
    }

    @DisplayName("Find employee by valid ID - should return expected DTO")
    @Test
    public void validId_whenGetEmployeeById_thenReturnsExpectedDto(){

        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.ofNullable(employee));
        Response<EmployeeDto> response = employeeService.getEmployeeById(employee.getId());
        check(response.getDto(), employee);

    }

    @DisplayName("Find classroom by invalid ID - should throw EntityNotExistingException")
    @Test
    public void invalidId_whenGetEmployeeById_thenThrowException(){

        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()->{

            employeeService.getEmployeeById(employee.getId());

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("Employee with given ID does not exists!");

    }

    @DisplayName("Get all employees - should return list of DTOs")
    @Test
    public void whenGetsAllEmployee_thenReturnsExpectedResponse(){

        int pageNumber = 1;

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Name 2");
        employee2.setTitle(Title.MD);
        employee2.setLastName("Last Name 2");
        employee2.setAcademicRank(AcademicRank.FULL_PROFESSOR);
        employee2.setDepartment(department);

        List<Employee> employeeList = List.of(employee,employee2);
        Page<Employee> page = new PageImpl<>(employeeList);

        Mockito.when(employeeRepository.findAll(PageRequest.of(1,10))).thenReturn(page);

        Response<List<EmployeeDto>> response = employeeService.getAllEmployees(1);
        assertThat(response.getDto()).isNotEmpty();
        assertThat(response.getDto()).isNotNull();
        check(response.getDto().get(0), employee);
        check(response.getDto().get(1), employee2);
        assertThat(response.getDto().size()).isEqualTo(2);

    }

    @DisplayName("Get all employees - should throw EmptyEntityListException if list is empty")
    @Test
    public void whenGetsAllEmployee_thenThrowException(){

        Page<Employee> page = new PageImpl<>(new ArrayList<>());

        Mockito.when(employeeRepository.findAll(PageRequest.of(1,10))).thenReturn(page);

        assertThatThrownBy(()->{

            employeeService.getAllEmployees(1);

        }).isInstanceOf(EmptyEntityListException.class)
                .hasMessageContaining("There are no employees!");




    }



}
