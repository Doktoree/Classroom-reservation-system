package doktoree.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import doktoree.backend.domain.*;
import doktoree.backend.dtos.ClassroomDto;
import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.ClassRoomType;
import doktoree.backend.enums.Role;
import doktoree.backend.enums.Title;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.factory.ReservationFactory;
import doktoree.backend.mapper.ClassroomMapper;
import doktoree.backend.mapper.ReservationMapper;
import doktoree.backend.repositories.*;
import doktoree.backend.security.AuthController;
import doktoree.backend.security.LoginDto;
import doktoree.backend.security.RegisterDto;
import doktoree.backend.services.MailServiceImpl;
import doktoree.backend.services.ReservationServiceImpl;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

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

    @Autowired
    private ReservationRepository reservationRepository;

    @MockBean
    private ReservationServiceImpl reservationService;

    @MockBean
    private MailServiceImpl mailService;

    private String token;

    private User user;

    private Reservation reservation;

    private Set<Classroom> classrooms;

    private Reservation reservation2;



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

        Classroom classroom = new Classroom();
        classroom.setCapacity(20);
        classroom.setClassRoomNumber("Classroom number");
        classroom.setNumberOfComputers(12);
        classroom.setClassRoomType(ClassRoomType.AMPHITHEATER);
        classroom = classroomRepository.save(classroom);

        Classroom classroom2 = new Classroom();
        classroom2.setCapacity(22);
        classroom2.setClassRoomNumber("Classroom number 2");
        classroom2.setNumberOfComputers(32);
        classroom2.setClassRoomType(ClassRoomType.AMPHITHEATER);
        classroom2 = classroomRepository.save(classroom2);

        classrooms = new HashSet<>(List.of(classroom,classroom2));

        Department department2 = new Department();
        department2.setShortName("dep1");
        department2.setName("Department1");
        Department savedDepartment2 = departmentRepository.save(department2);

        Employee employee2 = new Employee();
        employee2.setLastName("Last2");
        employee2.setTitle(Title.MD);
        employee2.setAcademicRank(AcademicRank.ASSISTANT_PROFESSOR);
        employee2.setName("Name2");
        employee2.setDepartment(savedDepartment2);
        employee2 = employeeRepository.save(employee2);

        User user2 = new User();
        user2.setPassword("pass");
        user2.setEmail("mejl");
        user2.setRole(Role.USER);
        user2.setEmployee(employee2);
        user2 = userRepository.save(user2);

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(22L);
        reservationDto.setDate(LocalDate.now());
        reservationDto.setStartTime(LocalTime.of(11,15));
        reservationDto.setEndTime(LocalTime.of(12,15));
        reservationDto.setClassrooms(classrooms);
        reservationDto.setReservationPurpose("EXAM");
        reservationDto.setSubjectName("Subject name");
        reservationDto.setUser(user2);

        reservation = ReservationFactory.createReservation(reservationDto);

        ReservationDto reservationDto2 = new ReservationDto();
        reservationDto2.setId(33L);
        reservationDto2.setDate(LocalDate.now());
        reservationDto2.setStartTime(LocalTime.of(14,15));
        reservationDto2.setEndTime(LocalTime.of(15,15));
        reservationDto2.setClassrooms(classrooms);
        reservationDto2.setReservationPurpose("COURSE");
        reservationDto2.setSubjectName("Subject name2");
        reservationDto2.setUser(user2);

        reservation2 = ReservationFactory.createReservation(reservationDto2);
    }

    @AfterEach
    public void tearDown(){

        reservationRepository.deleteAll();
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

        ResponseEntity<Map<String,Object>> response = authController.login(loginDto);
        Map<String,Object> map = response.getBody();
        token = (String)map.get("token");

    }

    public void check(ReservationDto dto, Reservation reservation){

        Assertions.assertThat(dto.getId()).isEqualTo(reservation.getId());
        Assertions.assertThat(dto.getDate()).isEqualTo(reservation.getDate());
        Assertions.assertThat(dto.getStartTime()).isEqualTo(reservation.getStartTime());
        Assertions.assertThat(dto.getUser().getId()).isEqualTo(reservation.getUser().getId());
        Assertions.assertThat(dto.getEndTime()).isEqualTo(reservation.getEndTime());
    }

    @Test
    public void whenGetReservation_thenReturnExpectedReservation() throws Exception{

        auth(Role.USER);
        Response<ReservationDto> response = new Response<>();
        response.setDtoT(ReservationMapper.mapToReservationDto(reservation));

        Mockito.when(reservationService.findReservationById(reservation.getId())).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation/" + reservation.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<ReservationDto> responseReservation = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ReservationDto>>() {
        });

        check(responseReservation.getDtoT(), reservation);


    }

    @Test
    public void whenGetReservation_thenThrowException() throws Exception{

        auth(Role.USER);

        Mockito.when(reservationService.findReservationById(reservation.getId())).thenThrow(new EntityNotExistingException("There is not reservation with given ID!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation/" + reservation.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());


    }

    @Test
    public void whenSaveReservation_thenReturnExpectedReservation() throws Exception {

        auth(Role.ADMIN);
        Response<ReservationDto> response =  new Response<>();
        response.setDtoT(ReservationMapper.mapToReservationDto(reservation));
        Mockito.when(reservationService.saveReservation(Mockito.any(ReservationDto.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservation/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationMapper.mapToReservationDto(reservation))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Response<ReservationDto> responseReservation = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ReservationDto>>(){
        });
        check(responseReservation.getDtoT(),reservation);



    }

    @Test
    public void whenSaveReservation_thenThrowException() throws Exception {

        auth(Role.USER);
        Mockito.when(reservationService.saveReservation(Mockito.any(ReservationDto.class))).thenThrow(new EntityNotSavedException("Reservation can not be saved!"));


        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservation/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationMapper.mapToReservationDto(reservation))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());



    }

    @Test
    public void whenGetAllReservations_thenReturnExpectedReservations() throws Exception{

        auth(Role.USER);
        Response<List<ReservationDto>> response = new Response<>();
        List<ReservationDto> list = List.of(reservation).stream().map(ReservationMapper::mapToReservationDto).toList();
        response.setDtoT(list);
        Mockito.when(reservationService.getAllReservations(0)).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation/")
                        .param("pageNumber","0")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<List<ReservationDto>> responseReservation = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<List<ReservationDto>>>() {
        });

        check(responseReservation.getDtoT().get(0), reservation);


    }

    @Test
    public void whenGetAllReservations_thenThrowException() throws Exception{

        auth(Role.USER);
        Mockito.when(reservationService.getAllReservations(0)).thenThrow(new EntityNotSavedException("Reservation can not be saved!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation/")
                        .param("pageNumber","0")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void whenUpdateReservation_thenReturnExpectedReservation() throws Exception {

        auth(Role.ADMIN);
        Response<ReservationDto> response =  new Response<>();
        response.setDtoT(ReservationMapper.mapToReservationDto(reservation));
        Mockito.when(reservationService.updateReservation(Mockito.any(ReservationDto.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationMapper.mapToReservationDto(reservation))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<ReservationDto> responseReservation = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ReservationDto>>(){
        });
        check(responseReservation.getDtoT(),reservation);


    }

    @Test
    public void whenUpdateClassroom_thenThrowException() throws Exception {

        auth(Role.ADMIN);
        Mockito.when(reservationService.updateReservation(Mockito.any(ReservationDto.class))).thenThrow(new EntityNotSavedException("Reservation can not be saved!"));


        mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationMapper.mapToReservationDto(reservation))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());



    }

    @Test
    public void whenUpdateReservation_thenReturnUnauthorizedStatus() throws Exception {

        auth(Role.USER);
        Response<ReservationDto> response =  new Response<>();
        response.setDtoT(ReservationMapper.mapToReservationDto(reservation));
        Mockito.when(reservationService.updateReservation(Mockito.any(ReservationDto.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationMapper.mapToReservationDto(reservation))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());


    }

    @Test
    public void whenGetAllReservationsFromUser_thenReturnExpectedDto() throws Exception{

        auth(Role.USER);
        Long userId = reservation.getUser().getId();
        Response<List<ReservationDto>> response = new Response<>();
        response.setDtoT(List.of(reservation, reservation2).stream().map(ReservationMapper::mapToReservationDto).toList());
        Mockito.when(reservationService.getAllReservationsFromUser(userId,0)).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation/user/" + userId)
                .param("pageNumber","0")
                .header("Authorization", "Bearer " + token )
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Response<List<ReservationDto>> responseReservation = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<List<ReservationDto>>>() {
        });

        check(responseReservation.getDtoT().get(0), reservation);
        check(responseReservation.getDtoT().get(1), reservation2);
    }

    @Test
    public void whenGetAllReservationsFromUser_thenThrowException() throws Exception{

        auth(Role.USER);
        Long userId = reservation.getUser().getId();
        Mockito.when(reservationService.getAllReservationsFromUser(userId,0)).thenThrow(new EmptyEntityListException("List of reservation is empty!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation/user/" + userId)
                        .param("pageNumber","0")
                        .header("Authorization", "Bearer " + token )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenGetAllAvailableClassrooms_thenReturnExpectedDto() throws Exception{

        auth(Role.USER);
        Response<List<ClassroomDto>> response =  new Response<>();
        response.setDtoT(classrooms.stream().toList().stream().map(ClassroomMapper::mapToClassroomDto).toList());
        Mockito.when(reservationService.getAllAvailableClassrooms(Mockito.any(ReservationDto.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservation/availableClassrooms")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationMapper.mapToReservationDto(reservation))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<List<ClassroomDto>> responseReservation = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<List<ClassroomDto>>>(){
        });
        Assertions.assertThat(responseReservation.getDtoT().size()).isEqualTo(2);


    }

    @Test
    public void whenGetAllAvailableClassrooms_thenThrowException() throws Exception{

        auth(Role.USER);
        Mockito.when(reservationService.getAllAvailableClassrooms(Mockito.any(ReservationDto.class))).thenThrow(new EmptyEntityListException("There are no available classrooms!"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservation/availableClassrooms")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationMapper.mapToReservationDto(reservation))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }


}
