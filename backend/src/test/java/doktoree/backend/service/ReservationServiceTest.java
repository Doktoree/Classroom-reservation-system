package doktoree.backend.service;

import doktoree.backend.domain.*;
import doktoree.backend.dtos.ClassroomDto;
import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.enums.ClassRoomType;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.exceptions.InvalidForeignKeyException;
import doktoree.backend.factory.ReservationFactory;
import doktoree.backend.mapper.ClassroomMapper;
import doktoree.backend.mapper.ReservationMapper;
import doktoree.backend.repositories.ClassroomRepository;
import doktoree.backend.repositories.ReservationNotificationRepository;
import doktoree.backend.repositories.ReservationRepository;
import doktoree.backend.repositories.UserRepository;
import doktoree.backend.services.MailService;
import doktoree.backend.services.ReservationServiceImpl;
import doktoree.backend.services.ReservationStatusServiceImpl;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationStatusServiceImpl reservationStatusService;

    @Mock
    private ReservationNotificationRepository notificationRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation, reservation2;

    private ReservationDto reservationDto;

    private User user;

    private Set<Classroom> classrooms;

    private List<Reservation> reservations;

    @BeforeEach
    public void setup(){

        user = new User();
        user.setId(2L);

        Classroom classroom1 = new Classroom();
        classroom1.setId(3L);

        Classroom classroom2 = new Classroom();
        classroom2.setId(4L);

        classrooms = new HashSet<>();
        classrooms.addAll(List.of(classroom1,classroom2));

        reservationDto = new ReservationDto();
        reservationDto.setId(1L);
        reservationDto.setDate(LocalDate.now());
        reservationDto.setEndTime(LocalTime.of(11,15));
        reservationDto.setStartTime(LocalTime.of(10,45));
        reservationDto.setClassrooms(classrooms);
        reservationDto.setUser(user);
        reservationDto.setSubjectName("Subject name 1");
        reservationDto.setReservationPurpose("EXAM");

        reservation = ReservationFactory.createReservation(reservationDto);
        reservation2 = reservation;

        reservations = List.of(reservation, reservation2);

    }

    public void check(ReservationDto dto, Reservation reservation){
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(reservation.getId());
        assertThat(dto.getDate()).isEqualTo(reservation.getDate());
        assertThat(dto.getUser().getId()).isEqualTo(reservation.getUser().getId());
        assertThat(dto.getEndTime()).isEqualTo(reservation.getEndTime());
        assertThat(dto.getStartTime()).isEqualTo(reservation.getStartTime());
        assertThat(dto.getClassrooms().size()).isEqualTo(reservation.getClassrooms().size());
    }

    @DisplayName("Find reservation by valid ID - should return expected DTO")
    @Test
    public void validId_whenFindReservationById_thenReturnsExpectedDto(){

        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        Response<ReservationDto> response = reservationService.findReservationById(reservation.getId());
        check(response.getDto(), reservation);


    }

    @DisplayName("Find reservation by invalid ID - should throw EntityNotExistingException")
    @Test
    public void invalidId_whenFindReservationById_thenThrowException(){

        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(()->{

            reservationService.findReservationById(reservation.getId());

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not reservation with given ID!");

    }

    @DisplayName("Save reservation - should return expected DTO")
    @Test
    public void whenSaveReservation_thenReturnsExpectedDto(){


        classrooms.forEach((c) -> {

            Mockito.when(classroomRepository.findById(c.getId())).thenReturn(Optional.of(c));

        });

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(reservation);
        Response<ReservationDto> response = reservationService.saveReservation(reservationDto);
        check(response.getDto(), reservation);


    }

    @DisplayName("Save reservation, invalid classroom ID - should throw InvalidForeignKeyException")
    @Test
    public void invalidClassroomId_whenSaveReservationNotification_thenThrowsException(){

        Mockito.when(classroomRepository.findById(classrooms.iterator().next().getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            reservationService.saveReservation(reservationDto);

        }).isInstanceOf(InvalidForeignKeyException.class)
                .hasMessageContaining("There is no classroom with given ID!");

    }

    @DisplayName("Save reservation, invalid user ID - should throw InvalidForeignKeyException")
    @Test
    public void invalidUserId_whenSaveReservationNotification_thenThrowsException(){

        classrooms.forEach((c) -> {

            Mockito.when(classroomRepository.findById(c.getId())).thenReturn(Optional.of(c));

        });
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            reservationService.saveReservation(reservationDto);

        }).isInstanceOf(InvalidForeignKeyException.class)
                .hasMessageContaining("There is no user with given ID!");

    }

    @DisplayName("Save classroom - should throw EntityNotSavedException")
    @Test
    public void whenSaveReservation_thenThrowsException(){

        classrooms.forEach((c) -> {

            Mockito.when(classroomRepository.findById(c.getId())).thenReturn(Optional.of(c));

        });
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenThrow(new EntityNotSavedException("Reservation can not be saved!"));

        assertThatThrownBy(() -> {

            reservationService.saveReservation(reservationDto);

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("Reservation can not be saved!");

    }

    @DisplayName("Get all reservations - should return list of DTOs")
    @Test
    public void whenGetAllReservations_thenReturnsExpectedDto(){

        Page<Reservation> page = new PageImpl<>(reservations);
        Mockito.when(reservationRepository.findAll(PageRequest.of(1,10))).thenReturn(page);
        Response<List<ReservationDto>> response = reservationService.getAllReservations(1);
        check(response.getDto().get(0), reservation);

    }

    @DisplayName("Get all reservations - should throw EmptyEntityListException")
    @Test
    public void whenGetAllReservations_thenThrowsException(){

        Page<Reservation> page = new PageImpl<>(new ArrayList<>());
        Mockito.when(reservationRepository.findAll(PageRequest.of(1,10))).thenReturn(page);

        assertThatThrownBy(() -> {

            reservationService.getAllReservations(1);

        }).isInstanceOf(EmptyEntityListException.class)
                .hasMessageContaining("There are no reservations!");

    }

    @DisplayName("Update reservation - should return expected DTO")
    @Test
    public void whenUpdateReservation_thenReturnsExpectedDto(){

        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        classrooms.forEach((c) -> {

            Mockito.when(classroomRepository.findById(c.getId())).thenReturn(Optional.of(c));

        });

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(reservation);
        Response<ReservationDto> response = reservationService.updateReservation(reservationDto);
        check(response.getDto(), reservation);

    }

    @DisplayName("Update classroom - should throw EntityNotExistingException")
    @Test
    public void invalidReservationId_whenUpdateReservation_thenThrowsException(){

        Mockito.when(reservationRepository.findById(reservationDto.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->{

            reservationService.updateReservation(reservationDto);

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not reservation with given ID!");

    }

    @DisplayName("Update classroom - should throw InvalidForeignKeyException")
    @Test
    public void invalidClassroomId_whenUpdateReservation_thenThrowsException(){

        Mockito.when(reservationRepository.findById(reservationDto.getId())).thenReturn(Optional.of(reservation));

        Mockito.when(classroomRepository.findById(classrooms.iterator().next().getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->{

            reservationService.updateReservation(reservationDto);

        }).isInstanceOf(InvalidForeignKeyException.class)
                .hasMessageContaining("There is no classroom with given ID!");


    }

    @DisplayName("Update classroom - should throw InvalidForeignKeyException")
    @Test
    public void invalidUserId_whenUpdateReservation_thenThrowsException(){

        Mockito.when(reservationRepository.findById(reservationDto.getId())).thenReturn(Optional.of(reservation));

        classrooms.forEach((c) -> {

            Mockito.when(classroomRepository.findById(c.getId())).thenReturn(Optional.of(c));

        });

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->{

            reservationService.updateReservation(reservationDto);

        }).isInstanceOf(InvalidForeignKeyException.class)
                .hasMessageContaining("There is no user with given ID!");


    }

    @DisplayName("Update classroom - should throw EntityNotSavedException")
    @Test
    public void whenUpdateReservation_thenThrowsException(){

        Mockito.when(reservationRepository.findById(reservationDto.getId())).thenReturn(Optional.of(reservation));

        classrooms.forEach((c) -> {

            Mockito.when(classroomRepository.findById(c.getId())).thenReturn(Optional.of(c));

        });

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThatThrownBy(() ->{

            reservationService.updateReservation(reservationDto);

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("Reservation can not be updated!");


    }

    @DisplayName("Get all reservations from user - should return expected DTO")
    @Test
    public void whenGetAllReservationsFromUser_thenReturnsExpectedDto(){

        Page<Reservation> page = new PageImpl<>(reservations);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(reservationRepository.findByUser(user,PageRequest.of(1,10))).thenReturn(page);
        Response<List<ReservationDto>> response = reservationService.getAllReservationsFromUser(user.getId(), 1);
        check(response.getDto().get(0), reservation);
        check(response.getDto().get(1), reservation2);

    }

    @DisplayName("Get all reservations from user - should throw EntityNotExistingException")
    @Test
    public void invalidUserId_whenGetAllReservationsFromUsers_thenThrowsException(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            reservationService.getAllReservationsFromUser(user.getId(),1);

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is no user with given ID");
    }

    @DisplayName("Get all reservations from user - should throw EmptyEntityListException")
    @Test
    public void whenGetAllReservationsFromUsers_thenThrowsException(){

        Page<Reservation> page = new PageImpl<>(new ArrayList<>());
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(reservationRepository.findByUser(user,PageRequest.of(1,10))).thenReturn(page);

        assertThatThrownBy(() -> {

            reservationService.getAllReservationsFromUser(user.getId(),1);

        }).isInstanceOf(EmptyEntityListException.class)
                .hasMessageContaining("List of reservation is empty!");
    }

    @DisplayName("Get all available classrooms - should return expected DTO")
    @Test
    public void whenGetAllAvailableClassrooms_thenReturnsExpectedDto(){

        Mockito.when(reservationRepository.findByDateAndStartTimeGreaterThanAndEndTimeLessThan(reservation.getDate(), reservation.getStartTime(), reservation.getEndTime()))
                .thenReturn(reservations);
        classrooms.forEach(c -> {

            Mockito.when(classroomRepository.findById(c.getId())).thenReturn(Optional.of(c));

        });

        Classroom c = new Classroom(33L, "Classroom number 1", ClassRoomType.COMPUTER_LAB, 10,11);
        List<Classroom> classrooms2 = new ArrayList<>(classrooms);
        classrooms2.add(c);
        Mockito.when(classroomRepository.findAll()).thenReturn(classrooms2);
        Response<List<ClassroomDto>> response = reservationService.getAllAvailableClassrooms(reservationDto);
        assertThat(response.getDto().get(0)).isEqualTo(ClassroomMapper.mapToClassroomDto(c));
    }

    @DisplayName("Get all available classrooms - should return expected DTO")
    @Test
    public void whenGetAllAvailableClassrooms_thenThrowsException(){

        Mockito.when(reservationRepository.findByDateAndStartTimeGreaterThanAndEndTimeLessThan(reservation.getDate(), reservation.getStartTime(), reservation.getEndTime()))
                .thenReturn(reservations);
        classrooms.forEach(c -> {

            Mockito.when(classroomRepository.findById(c.getId())).thenReturn(Optional.of(c));

        });


        List<Classroom> classrooms2 = new ArrayList<>();
        Mockito.when(classroomRepository.findAll()).thenReturn(classrooms2);

        assertThatThrownBy(() -> {

            reservationService.getAllAvailableClassrooms(reservationDto);

        }).isInstanceOf(EmptyEntityListException.class)
                .hasMessageContaining("There are no available classrooms!");

    }


}
