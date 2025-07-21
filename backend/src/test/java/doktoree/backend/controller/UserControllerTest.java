package doktoree.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import doktoree.backend.domain.*;
import doktoree.backend.dtos.UserDto;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.Role;
import doktoree.backend.enums.Title;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotDeletedException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.mapper.UserMapper;
import doktoree.backend.repositories.*;
import doktoree.backend.security.AuthController;
import doktoree.backend.security.LoginDto;
import doktoree.backend.security.RegisterDto;
import doktoree.backend.services.MailServiceImpl;
import doktoree.backend.services.UserServiceImpl;
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
public class UserControllerTest {

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
    private UserServiceImpl userService;

    @MockBean
    private MailServiceImpl mailService;

    private String token;

    private User user;

    private User savedUser;

    @BeforeEach
    public void setup() {

        Department department = new Department();
        department.setShortName("dep");
        department.setName("Department");
        Department savedDepartment = departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setLastName("Last");
        employee.setTitle(Title.MD);
        employee.setAcademicRank(AcademicRank.FULL_PROFESSOR);
        employee.setName("Name");
        employee.setDepartment(savedDepartment);
        employee = employeeRepository.save(employee);

        user = new User();
        user.setPassword("pass");
        user.setEmail("mail@gmail.com");
        user.setEmployee(employee);

        savedUser = new User();
        savedUser.setRole(Role.USER);
        savedUser.setPassword("pass");
        savedUser.setEmail("mejl@gmail.com");
        savedUser.setEmployee(employee);
        savedUser = userRepository.save(savedUser);

    }

    @AfterEach
    public void tearDown(){

        userRepository.deleteAll();
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    public void auth(Role role){

        user.setRole(role);
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail(user.getEmail());
        registerDto.setPassword(user.getPassword());
        registerDto.setRole(user.getRole());
        registerDto.setEmployeeId(user.getEmployee().getId());
        authController.register(registerDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("mail@gmail.com");
        loginDto.setPassword("pass");

        ResponseEntity<Map<String,String>> response = authController.login(loginDto);
        Map<String,String> map = response.getBody();
        token = map.get("token");

    }

    public void check(UserDto dto, User user){

        Assertions.assertThat(dto.getId()).isEqualTo(user.getId());
        Assertions.assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(dto.getEmployeeId()).isEqualTo(user.getEmployee().getId());
        Assertions.assertThat(dto.getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(dto.getRole()).isEqualTo(user.getRole());

    }

    @Test
    public void whenFindUserById_thenReturnExpectedDto() throws Exception{

        auth(Role.USER);
        Response<UserDto> response = new Response<>();
        response.setDtoT(UserMapper.mapToUserDto(savedUser));
        Mockito.when(userService.findUserById(savedUser.getId())).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/" + savedUser.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<UserDto> responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<UserDto>>() {
        });

        check(responseUser.getDtoT(), savedUser);

    }

    @Test
    public void whenFindUserById_thenThrowException() throws Exception{

        auth(Role.USER);
        Mockito.when(userService.findUserById(savedUser.getId())).thenThrow(new EntityNotExistingException("There is not user with given ID!"));

         mockMvc.perform(MockMvcRequestBuilders.get("/api/user/" + savedUser.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void whenSaveUser_thenReturnExpectedUser() throws Exception {

        auth(Role.ADMIN);
        Response<UserDto> response =  new Response<>();
        response.setDtoT(UserMapper.mapToUserDto(savedUser));
        Mockito.when(userService.saveUser(Mockito.any(UserDto.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserMapper.mapToUserDto(savedUser))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Response<UserDto> responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<UserDto>>(){
        });
        check(responseUser.getDtoT(),savedUser);



    }

    @Test
    public void whenSaveUser_thenThrowException() throws Exception {

        auth(Role.ADMIN);
        Mockito.when(userService.saveUser(Mockito.any(UserDto.class))).thenThrow(new EntityNotSavedException("User can not be saved!"));

       mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserMapper.mapToUserDto(savedUser))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());



    }

    @Test
    public void whenSaveUser_thenReturnUnauthorizedStatus() throws Exception {

        auth(Role.USER);
        Response<UserDto> response =  new Response<>();
        response.setDtoT(UserMapper.mapToUserDto(savedUser));
        Mockito.when(userService.saveUser(Mockito.any(UserDto.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserMapper.mapToUserDto(savedUser))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());



    }

    @Test
    public void whenDeleteUser_thenReturnExpectedUser() throws Exception {

        auth(Role.ADMIN);
        Response<UserDto> response =  new Response<>();
        response.setDtoT(UserMapper.mapToUserDto(savedUser));
        Mockito.when(userService.deleteUser(savedUser.getId())).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/" + savedUser.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserMapper.mapToUserDto(savedUser))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<UserDto> responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<UserDto>>(){
        });
        check(responseUser.getDtoT(),savedUser);



    }

    @Test
    public void whenDeleteUser_thenThrowException() throws Exception {

        auth(Role.ADMIN);
        Response<UserDto> response =  new Response<>();
        response.setDtoT(UserMapper.mapToUserDto(savedUser));
        Mockito.when(userService.deleteUser(savedUser.getId())).thenThrow(new EntityNotDeletedException("There is not user with given ID!"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/" + savedUser.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserMapper.mapToUserDto(savedUser))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }

    @Test
    public void whenDeleteUser_thenReturnUnauthorizedStatus() throws Exception {

        auth(Role.USER);
        Response<UserDto> response =  new Response<>();
        response.setDtoT(UserMapper.mapToUserDto(savedUser));
        Mockito.when(userService.deleteUser(savedUser.getId())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/" + savedUser.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserMapper.mapToUserDto(savedUser))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());



    }

    @Test
    public void whenGetAllReservations_thenReturnExpectedReservations() throws Exception{

        auth(Role.ADMIN);
        Response<List<UserDto>> response = new Response<>();
        List<UserDto> list = List.of(savedUser).stream().map(UserMapper::mapToUserDto).toList();
        response.setDtoT(list);
        Mockito.when(userService.getAllUsers(0)).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/all")
                        .param("pageNumber","0")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<List<UserDto>> responseReservation = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<List<UserDto>>>() {
        });

        check(responseReservation.getDtoT().get(0), savedUser);


    }

    @Test
    public void whenGetAllReservations_thenThrowException() throws Exception{

        auth(Role.ADMIN);
        Mockito.when(userService.getAllUsers(0)).thenThrow(new EmptyEntityListException("There are no users!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/all")
                        .param("pageNumber","0")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }

    @Test
    public void whenGetAllReservations_thenReturnUnAuthorizedStatus() throws Exception{

        auth(Role.USER);
        Response<List<UserDto>> response = new Response<>();
        List<UserDto> list = List.of(savedUser).stream().map(UserMapper::mapToUserDto).toList();
        response.setDtoT(list);
        Mockito.when(userService.getAllUsers(0)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/all")
                        .param("pageNumber","0")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());


    }

    @Test
    public void whenUpdateUser_thenReturnExpectedUser() throws Exception {

        auth(Role.ADMIN);
        Response<UserDto> response =  new Response<>();
        response.setDtoT(UserMapper.mapToUserDto(savedUser));
        Mockito.when(userService.updateUser(Mockito.any(UserDto.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/user")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserMapper.mapToUserDto(savedUser))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<UserDto> responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<UserDto>>(){
        });
        check(responseUser.getDtoT(),savedUser);



    }

    @Test
    public void whenUpdateUser_thenThrowException() throws Exception {

        auth(Role.ADMIN);
        Mockito.when(userService.updateUser(Mockito.any(UserDto.class))).thenThrow(new EntityNotSavedException("\"User can not be updated!"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserMapper.mapToUserDto(savedUser))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void whenUpdateUser_thenReturnUnauthorizedStatus() throws Exception {

        auth(Role.USER);
        Response<UserDto> response =  new Response<>();
        response.setDtoT(UserMapper.mapToUserDto(savedUser));
        Mockito.when(userService.updateUser(Mockito.any(UserDto.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserMapper.mapToUserDto(savedUser))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());



    }
}




