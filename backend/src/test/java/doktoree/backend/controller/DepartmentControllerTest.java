package doktoree.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import doktoree.backend.domain.Department;
import doktoree.backend.domain.Employee;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.DepartmentDto;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.Role;
import doktoree.backend.enums.Title;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.mapper.DepartmentMapper;
import doktoree.backend.repositories.DepartmentRepository;
import doktoree.backend.repositories.EmployeeRepository;
import doktoree.backend.repositories.UserRepository;
import doktoree.backend.security.AuthController;
import doktoree.backend.security.LoginDto;
import doktoree.backend.security.RegisterDto;
import doktoree.backend.services.DepartmentServiceImpl;
import doktoree.backend.services.MailServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AuthController authController;

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @MockBean
  private DepartmentServiceImpl departmentService;

  @MockBean
  private MailServiceImpl mailService;

  private User adminUser;

  private String token;

  private Department department;

  private Employee employee;

  @BeforeEach
  public void setup() {
    department = new Department();
    department.setName("Department");
    department.setShortName("Dep1");
    department.setEmployees(new ArrayList<>());
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
  public void tearDown() {
    userRepository.deleteAll();
    departmentRepository.deleteAll();
    employeeRepository.deleteAll();
  }

  public void auth(Role role) {

    RegisterDto registerDto = new RegisterDto();
    registerDto.setPassword("pass");
    registerDto.setEmail("mail@gmail.com");
    registerDto.setRole(role);
    registerDto.setEmployeeId(employee.getId());
    authController.register(registerDto);

    LoginDto loginDto = new LoginDto();
    loginDto.setPassword("pass");
    loginDto.setEmail("mail@gmail.com");

    ResponseEntity<Map<String, Object>> responseEntity = authController.login(loginDto);
    Map<String,Object> map = responseEntity.getBody();
    token = (String)map.get("token");
  }

  public void check(DepartmentDto dto, Department department) {
    Assertions.assertThat(dto.getId()).isEqualTo(department.getId());
    Assertions.assertThat(dto.getName()).isEqualTo(department.getName());
    Assertions.assertThat(dto.getShortName()).isEqualTo(department.getShortName());
  }

  @Test
  public void whenGetAllDepartments_thenReturnExpectedDepartments() throws Exception {
    auth(Role.ADMIN);

    Department dep = new Department();
    dep.setShortName("Dep");
    dep.setName("Department 1");
    dep.setEmployees(new ArrayList<>());
    dep = departmentRepository.save(dep);

    List<DepartmentDto> list = List.of(department,dep).stream().map(DepartmentMapper::mapToDepartmentDto).toList();
    Response<List<DepartmentDto>> response = new Response<>();
    response.setDtoT(list);
    Mockito.when(departmentService.getAllDepartments()).thenReturn(response);


    Mockito.when(departmentService.getAllDepartments()).thenReturn(response);

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/department/")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    Response<List<DepartmentDto>> responseResult = objectMapper.readValue(
        result.getResponse().getContentAsString(),
        new TypeReference<Response<List<DepartmentDto>>>() {}
    );

    check(responseResult.getDtoT().get(0), department);
  }

  @Test
  public void whenGetAllDepartments_thenThrowException() throws Exception {
    auth(Role.ADMIN);

    Mockito.when(departmentService.getAllDepartments())
        .thenThrow(new EmptyEntityListException("There are no departments!"));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/department/")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }


}
