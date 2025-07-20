package doktoree.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import doktoree.backend.domain.Classroom;
import doktoree.backend.domain.Department;
import doktoree.backend.domain.Employee;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.ClassroomDto;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.ClassRoomType;
import doktoree.backend.enums.Role;
import doktoree.backend.enums.Title;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotDeletedException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.mapper.ClassroomMapper;
import doktoree.backend.repositories.ClassroomRepository;
import doktoree.backend.repositories.DepartmentRepository;
import doktoree.backend.repositories.EmployeeRepository;
import doktoree.backend.repositories.UserRepository;
import doktoree.backend.security.AuthController;
import doktoree.backend.security.LoginDto;
import doktoree.backend.security.RegisterDto;
import doktoree.backend.services.ClassroomService;
import doktoree.backend.services.ClassroomServiceImpl;
import doktoree.backend.services.MailServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
public class ClassroomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private AuthController authController;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private ClassroomServiceImpl classroomService;

    @MockBean
    private MailServiceImpl mailService;

    private Classroom classroom, classroom1;

    private String token;

    private User user;

    @BeforeEach
    public void setup(){

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

        classroom = new Classroom();
        classroom.setId(1L);
        classroom.setNumberOfComputers(20);
        classroom.setCapacity(40);
        classroom.setClassRoomType(ClassRoomType.AMPHITHEATER);
        classroom.setClassRoomNumber("Classroom number1");

        classroom1 = new Classroom();
        classroom1.setId(11L);
        classroom1.setNumberOfComputers(22);
        classroom1.setCapacity(44);
        classroom1.setClassRoomType(ClassRoomType.COMPUTER_LAB);
        classroom1.setClassRoomNumber("Classroom number2");

    }

    @AfterEach
    public void tearDown(){

        userRepository.deleteAll();
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
        classroomRepository.deleteAll();
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

        ResponseEntity< Map<String,String>> response = authController.login(loginDto);
        Map<String,String> map = response.getBody();
        token = map.get("token");

    }

    public void check(ClassroomDto dto, Classroom classroom){

        Assertions.assertThat(dto.getClassRoomNumber()).isEqualTo(classroom.getClassRoomNumber());
        Assertions.assertThat(dto.getCapacity()).isEqualTo(classroom.getCapacity());
        Assertions.assertThat(dto.getNumberOfComputers()).isEqualTo(classroom.getNumberOfComputers());
        Assertions.assertThat(dto.getClassRoomType()).isEqualTo(classroom.getClassRoomType());
        Assertions.assertThat(dto.getId()).isEqualTo(classroom.getId());

    }

    @Test
    public void whenFindClassroomById_thenReturnExpectedClassroom() throws Exception {

        auth(Role.USER);
        Response<ClassroomDto> response = new Response<>();
        response.setDto(ClassroomMapper.mapToClassroomDto(classroom));
        Mockito.when(classroomService.findClassroomById(1L)).thenReturn(response);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/classroom/" + classroom.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<ClassroomDto> responseClassroom = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference< Response<ClassroomDto>>() {
        });

        check(responseClassroom.getDto(), classroom);


    }

    @Test
    public void whenFindClassroomById_thenThrowException() throws Exception {

        auth(Role.USER);
        Mockito.when(classroomService.findClassroomById(classroom.getId())).thenThrow(new EntityNotExistingException("There is not classroom with given ID!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/classroom/" + classroom.getId())
                        .header("Authorization","Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());


    }

    @Test
    public void whenSaveClassroom_thenReturnExpectedClassroom() throws Exception {

        auth(Role.ADMIN);
        Response<ClassroomDto> response =  new Response<>();
        response.setDto(ClassroomMapper.mapToClassroomDto(classroom));
        Mockito.when(classroomService.saveClassroom(Mockito.any(ClassroomDto.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/classroom")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(classroom)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Response<ClassroomDto> responseClassroom = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ClassroomDto>>(){
        });
        check(responseClassroom.getDto(),classroom);



    }

    @Test
    public void whenSaveClassroom_thenThrowException() throws Exception{

        auth(Role.ADMIN);
        Mockito.when(classroomService.saveClassroom(Mockito.any(ClassroomDto.class))).thenThrow(new EntityNotSavedException("Classroom can not be saved!"));


        mockMvc.perform(MockMvcRequestBuilders.post("/api/classroom")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(classroom)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }

    @Test
    public void whenSaveClassroom_thenReturnUnauthorizedStatus() throws Exception{

        auth(Role.USER);
        Response<ClassroomDto> response =  new Response<>();
        response.setDto(ClassroomMapper.mapToClassroomDto(classroom));
        Mockito.when(classroomService.saveClassroom(Mockito.any(ClassroomDto.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/classroom")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(classroom)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());




    }

    @Test
    public void whenDeleteClassroom_thenReturnExpectedClassroom() throws Exception{

        auth(Role.ADMIN);
        Response<ClassroomDto> response =  new Response<>();
        response.setDto(ClassroomMapper.mapToClassroomDto(classroom));
        Mockito.when(classroomService.deleteClassroom(classroom.getId())).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/classroom/" + classroom.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(classroom)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<ClassroomDto> responseClassroom = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ClassroomDto>>() {
        });

        check(responseClassroom.getDto(),classroom);


    }

    @Test
    public void whenDeleteClassroom_thenThrowException() throws Exception{

        auth(Role.ADMIN);
        Mockito.when(classroomService.deleteClassroom(classroom.getId())).thenThrow(new EntityNotDeletedException("Classroom can not be deleted"));


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/classroom/" + classroom.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(classroom)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }

    @Test
    public void whenDeleteClassroom_thenReturnUnauthorizedStatus() throws Exception{

        auth(Role.USER);
        Response<ClassroomDto> response =  new Response<>();
        response.setDto(ClassroomMapper.mapToClassroomDto(classroom));
        Mockito.when(classroomService.deleteClassroom(classroom.getId())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/classroom/" + classroom.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(objectMapper.writeValueAsString(classroom)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());




    }

    @Test
    public void whenGetAllClassrooms_thenReturnExpectedClassrooms() throws Exception{

        auth(Role.USER);
        Response<List<ClassroomDto>> response = new Response<>();
        List<ClassroomDto> list = List.of(classroom,classroom1).stream().map(ClassroomMapper::mapToClassroomDto).toList();
        response.setDto(list);
        Mockito.when(classroomService.getAllClassrooms()).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/classroom/all")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<List<ClassroomDto>> responseClassroom = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<List<ClassroomDto>>>() {
        });

        check(responseClassroom.getDto().get(0), classroom);
        check(responseClassroom.getDto().get(1), classroom1);


    }

    @Test
    public void whenGetAllClassrooms_thenThrowException() throws Exception{

        auth(Role.USER);
        Mockito.when(classroomService.getAllClassrooms()).thenThrow(new EmptyEntityListException("There are no classrooms!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/classroom/all")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());



    }

    @Test
    public void whenUpdateClassroom_thenReturnExpectedClassroom() throws Exception {

        auth(Role.ADMIN);
        classroom1.setId(classroom.getId());
        Response<ClassroomDto> response =  new Response<>();
        response.setDto(ClassroomMapper.mapToClassroomDto(classroom1));
        Mockito.when(classroomService.updateClassroom(Mockito.any(ClassroomDto.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/classroom")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(classroom1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<ClassroomDto> responseClassroom = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ClassroomDto>>(){
        });
        check(responseClassroom.getDto(),classroom1);



    }

    @Test
    public void whenUpdateClassroom_thenThrowException() throws Exception{

        auth(Role.ADMIN);
        Mockito.when(classroomService.updateClassroom(Mockito.any(ClassroomDto.class))).thenThrow(new EntityNotSavedException("Classroom can not be saved!"));


        mockMvc.perform(MockMvcRequestBuilders.patch("/api/classroom")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(classroom)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }

    @Test
    public void whenUpdateClassroom_thenReturnUnauthorizedStatus() throws Exception{

        auth(Role.USER);
        Response<ClassroomDto> response =  new Response<>();
        response.setDto(ClassroomMapper.mapToClassroomDto(classroom));
        Mockito.when(classroomService.updateClassroom(Mockito.any(ClassroomDto.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/classroom")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(classroom)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

}
