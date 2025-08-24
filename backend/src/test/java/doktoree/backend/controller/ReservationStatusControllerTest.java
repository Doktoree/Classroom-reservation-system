package doktoree.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import doktoree.backend.domain.*;
import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.dtos.ReservationStatusDto;
import doktoree.backend.enums.*;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.factory.ReservationFactory;
import doktoree.backend.mapper.ReservationStatusMapper;
import doktoree.backend.repositories.*;
import doktoree.backend.security.AuthController;
import doktoree.backend.security.LoginDto;
import doktoree.backend.security.RegisterDto;
import doktoree.backend.services.MailServiceImpl;
import doktoree.backend.services.ReservationStatusServiceImpl;
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
public class ReservationStatusControllerTest {

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

    @Autowired
    private ReservationStatusRepository reservationStatusRepository;

    @MockBean
    private ReservationStatusServiceImpl reservationStatusService;

    @MockBean
    private MailServiceImpl mailService;

    private String token;

    private User user;

    private Reservation reservation;

    private ReservationStatus reservationStatus;

    private List<ReservationStatus> reservationStatuses;

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

        Set<Classroom> classrooms = new HashSet<>(List.of(classroom,classroom2));

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
        reservation = reservationRepository.save(reservation);

        reservationStatus = new ReservationStatus();
        reservationStatus.setReservation(reservation);
        reservationStatus.setStatus(Status.PENDING);
        reservationStatus.setId(reservation.getId());

        ReservationStatus reservationStatus2 = new ReservationStatus();
        reservationStatus2.setReservation(reservation);
        reservationStatus2.setStatus(Status.APPROVED);
        reservationStatus2.setId(reservation.getId());

        reservationStatuses = List.of(reservationStatus,reservationStatus2);

    }

    @AfterEach
    public void tearDown(){

        reservationStatusRepository.deleteAll();
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

    public void check(ReservationStatusDto dto, ReservationStatus reservationStatus){

        Assertions.assertThat(dto.getId()).isEqualTo(reservationStatus.getId());
        Assertions.assertThat(dto.getReservationDto().getId()).isEqualTo(reservationStatus.getReservation().getId());
        Assertions.assertThat(dto.getStatus()).isEqualTo(reservationStatus.getStatus());
    }

    @Test
    public void whenGetReservationStatus_thenReturnExpectedReservationStatus() throws Exception{

        auth(Role.USER);
        Response<ReservationStatusDto> response = new Response<>();
        response.setDtoT(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        Mockito.when(reservationStatusService.findReservationStatusById(reservationStatus.getId())).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation-status/" + reservationStatus.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<ReservationStatusDto> responseReservationStatus = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ReservationStatusDto>>() {
        });

        check(responseReservationStatus.getDtoT(), reservationStatus);


    }

    @Test
    public void whenGetReservationStatus_thenThrowException() throws Exception{

        auth(Role.USER);
        Mockito.when(reservationStatusService.findReservationStatusById(reservationStatus.getId())).thenThrow(new EntityNotExistingException("There is no reservation status with given ID"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation-status/" + reservationStatus.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());


    }

    @Test
    public void whenGetAllReservations_thenReturnExpectedReservations() throws Exception{

        Long userId = reservationStatus.getReservation().getUser().getId();
        auth(Role.USER);
        Response<List<ReservationStatusDto>> response = new Response<>();
        List<ReservationStatusDto> list = reservationStatuses.stream().map(ReservationStatusMapper::mapToReservationStatusDto).toList();
        response.setDtoT(list);
        Mockito.when(reservationStatusService.getAllReservationStatusFromUser(userId)).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation-status/user/" + userId )
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<List<ReservationStatusDto>> responseReservationStatus = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<List<ReservationStatusDto>>>() {
        });

        check(responseReservationStatus.getDtoT().get(0), reservationStatus);


    }

    @Test
    public void whenGetAllReservations_thenThrowException() throws Exception{

        Long userId = reservationStatus.getReservation().getUser().getId();
        auth(Role.USER);
        Mockito.when(reservationStatusService.getAllReservationStatusFromUser(userId)).thenThrow(new EmptyEntityListException("There are not reservations from given user!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation-status/user/" + userId )
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }

    @Test
    public void whenSaveReservationStatus_thenReturnExpectedReservationStatus() throws Exception {

        auth(Role.ADMIN);
        Response<ReservationStatusDto> response =  new Response<>();
        response.setDtoT(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));
        Mockito.when(reservationStatusService.saveReservationStatus(Mockito.any(ReservationStatusDto.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/reservation-status/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Response<ReservationStatusDto> responseReservation = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ReservationStatusDto>>(){
        });
        check(responseReservation.getDtoT(),reservationStatus);



    }

    @Test
    public void whenSaveReservationStatus_thenThrowException() throws Exception {

        auth(Role.ADMIN);
        Mockito.when(reservationStatusService.saveReservationStatus(Mockito.any(ReservationStatusDto.class))).thenThrow(new EntityNotSavedException("Reservation can not be saved!"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservation-status/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());



    }

    @Test
    public void whenSaveReservationStatus_thenReturnUnAuthorizedStatus() throws Exception {

        auth(Role.USER);
        Response<ReservationStatusDto> response =  new Response<>();
        response.setDtoT(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));
        Mockito.when(reservationStatusService.saveReservationStatus(Mockito.any(ReservationStatusDto.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservation-status/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());



    }

    @Test
    public void whenApproveReservation_thenReturnExpectedReservationStatus() throws Exception {

        auth(Role.ADMIN);
        reservationStatus.setStatus(Status.APPROVED);
        Response<ReservationStatusDto> response =  new Response<>();
        response.setDtoT(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));
        Mockito.when(reservationStatusService.approveReservation(Mockito.any(ReservationStatusDto.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation-status/approve")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<ReservationStatusDto> responseReservation = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ReservationStatusDto>>(){
        });
        reservationStatus.setStatus(Status.APPROVED);
        check(responseReservation.getDtoT(),reservationStatus);



    }

    @Test
    public void whenApproveReservation_thenThrowException() throws Exception {

        auth(Role.ADMIN);
        Mockito.when(reservationStatusService.approveReservation(Mockito.any(ReservationStatusDto.class))).thenThrow(new EntityNotSavedException("Reservation status can not be saved!"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation-status/approve")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());



    }

    @Test
    public void whenApproveReservation_thenReturnUnauthorizedStatus() throws Exception {

        auth(Role.USER);
        reservationStatus.setStatus(Status.APPROVED);
        Response<ReservationStatusDto> response =  new Response<>();
        response.setDtoT(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));
        Mockito.when(reservationStatusService.approveReservation(Mockito.any(ReservationStatusDto.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation-status/approve")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());



    }

    @Test
    public void whenRejectReservation_thenReturnExpectedReservationStatus() throws Exception {

        auth(Role.ADMIN);
        reservationStatus.setStatus(Status.REJECTED);
        Response<ReservationStatusDto> response =  new Response<>();
        response.setDtoT(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));
        Mockito.when(reservationStatusService.rejectReservation(Mockito.any(ReservationStatusDto.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation-status/reject")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Response<ReservationStatusDto> responseReservation = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ReservationStatusDto>>(){
        });
        reservationStatus.setStatus(Status.REJECTED);
        check(responseReservation.getDtoT(),reservationStatus);



    }

    @Test
    public void whenRejectReservation_thenThrowException() throws Exception {

        auth(Role.ADMIN);
        Mockito.when(reservationStatusService.rejectReservation(Mockito.any(ReservationStatusDto.class))).thenThrow(new EntityNotSavedException("Reservation status can not be saved!"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation-status/reject")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());



    }

    @Test
    public void whenRejectReservation_thenReturnUnauthorizedStatus() throws Exception {

        auth(Role.USER);
        reservationStatus.setStatus(Status.REJECTED);
        Response<ReservationStatusDto> response =  new Response<>();
        response.setDtoT(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));
        Mockito.when(reservationStatusService.rejectReservation(Mockito.any(ReservationStatusDto.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservation-status/reject")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());



    }
}
