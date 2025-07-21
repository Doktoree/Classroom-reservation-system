package doktoree.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import doktoree.backend.domain.Department;
import doktoree.backend.domain.Employee;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.Role;
import doktoree.backend.enums.Title;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.mapper.EmployeeMapper;
import doktoree.backend.repositories.DepartmentRepository;
import doktoree.backend.repositories.EmployeeRepository;
import doktoree.backend.repositories.UserRepository;
import doktoree.backend.security.AuthController;
import doktoree.backend.security.LoginDto;
import doktoree.backend.security.RegisterDto;
import doktoree.backend.services.EmployeeServiceImpl;
import doktoree.backend.services.MailServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthController authController;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private EmployeeServiceImpl employeeService;

    @MockBean
    private MailServiceImpl mailService;

    private User user;

    private Employee employee;

    private String token;

    private Department department;

    @BeforeEach
    public void setup(){

        department = new Department();
        department.setName("Department");
        department.setShortName("Dep1");
        department = departmentRepository.save(department);

        employee = new Employee();
        employee.setTitle(Title.JD);
        employee.setDepartment(department);
        employee.setAcademicRank(AcademicRank.FULL_PROFESSOR);
        employee.setName("Name");
        employee.setLastName("Last");
        employee = employeeRepository.save(employee);




    }

    @AfterEach
    public void tearDown(){

        userRepository.deleteAll();
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    public void auth(Role role){

        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmployeeId(employee.getId());
        registerDto.setPassword("pass");
        registerDto.setEmail("mail@gmail.com");
        registerDto.setRole(role);

        authController.register(registerDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setPassword("pass");
        loginDto.setEmail("mail@gmail.com");

        ResponseEntity<Map<String, String>> responseEntity = authController.login(loginDto);
        Map<String,String> map = responseEntity.getBody();
        token = map.get("token");

    }

    public void check(EmployeeDto dto, Employee employee){

        Assertions.assertThat(dto.getDepartmentId()).isEqualTo(employee.getDepartment().getId());
        Assertions.assertThat(dto.getLastName()).isEqualTo(employee.getLastName());
        Assertions.assertThat(dto.getName()).isEqualTo(employee.getName());
        Assertions.assertThat(dto.getTitle()).isEqualTo(employee.getTitle());
        Assertions.assertThat(dto.getAcademicRank()).isEqualTo(employee.getAcademicRank());
        Assertions.assertThat(dto.getId()).isEqualTo(employee.getId());


    }

    @Test
    public void whenFindEmployeeIdById_thenReturnExpectedEmployee() throws Exception{

        auth(Role.ADMIN);
        Response<EmployeeDto> response = new Response<>();
        response.setDtoT(EmployeeMapper.mapToEmployeeDto(employee));
        Mockito.when(employeeService.getEmployeeById(employee.getId())).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/" + employee.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<EmployeeDto> response1 = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<EmployeeDto>>() {
        });

        check(response1.getDtoT(), employee);


    }

    @Test
    public void whenFindEmployeeIdById_thenThrowException() throws Exception{

        auth(Role.ADMIN);
        Mockito.when(employeeService.getEmployeeById(employee.getId())).thenThrow(new EntityNotExistingException("Employee with given ID does not exists!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/" + employee.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());



    }

    @Test
    public void whenFindEmployeeIdById_thenReturnUnauthorizedStatus() throws Exception{

        auth(Role.USER);
        Response<EmployeeDto> response = new Response<>();
        response.setDtoT(EmployeeMapper.mapToEmployeeDto(employee));
        Mockito.when(employeeService.getEmployeeById(employee.getId())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/" + employee.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    public void whenGetAllEmployees_thenReturnExpectedEmployees() throws Exception{

        auth(Role.ADMIN);
        Employee employee2 = new Employee();
        employee2.setTitle(Title.MASTER);
        employee2.setDepartment(department);
        employee2.setAcademicRank(AcademicRank.TEACHING_ASSISTANT);
        employee2.setName("Name2");
        employee2.setLastName("Last2");
        employee2 = employeeRepository.save(employee2);

        List<EmployeeDto> listDto = List.of(employee,employee2).stream().map(EmployeeMapper::mapToEmployeeDto).toList();
        Response<List<EmployeeDto>> response = new Response<>();
        response.setDtoT(listDto);
        Mockito.when(employeeService.getAllEmployees(0)).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/")
                        .param("pageNumber","0")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<List<EmployeeDto>> listResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<List<EmployeeDto>>>() {
        });

        check(listResponse.getDtoT().get(0), employee);
        check(listResponse.getDtoT().get(1), employee2);

    }

    @Test
    public void whenGetAllEmployees_thenThrowException() throws Exception{

        auth(Role.ADMIN);
        Employee employee2 = new Employee();
        employee2.setTitle(Title.MASTER);
        employee2.setDepartment(department);
        employee2.setAcademicRank(AcademicRank.TEACHING_ASSISTANT);
        employee2.setName("Name2");
        employee2.setLastName("Last2");
        employee2 = employeeRepository.save(employee2);

        Mockito.when(employeeService.getAllEmployees(0)).thenThrow(new EmptyEntityListException("There are no employees!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/")
                        .param("pageNumber","0")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }

    @Test
    public void whenGetAllEmployees_thenReturnUnAuthorizedStatus() throws Exception{

        auth(Role.USER);
        Employee employee2 = new Employee();
        employee2.setTitle(Title.MASTER);
        employee2.setDepartment(department);
        employee2.setAcademicRank(AcademicRank.TEACHING_ASSISTANT);
        employee2.setName("Name2");
        employee2.setLastName("Last2");
        employee2 = employeeRepository.save(employee2);

        List<EmployeeDto> listDto = List.of(employee,employee2).stream().map(EmployeeMapper::mapToEmployeeDto).toList();
        Response<List<EmployeeDto>> response = new Response<>();
        response.setDtoT(listDto);
        Mockito.when(employeeService.getAllEmployees(0)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/")
                        .param("pageNumber","0")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
