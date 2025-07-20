package doktoree.backend.service;

import doktoree.backend.domain.*;
import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.dtos.ReservationNotificationDto;
import doktoree.backend.enums.Status;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.mapper.ReservaitonNotificationMapper;
import doktoree.backend.repositories.ReservationNotificationRepository;
import doktoree.backend.repositories.ReservationRepository;
import doktoree.backend.repositories.ReservationStatusRepository;
import doktoree.backend.repositories.UserRepository;
import doktoree.backend.services.ReservationNotificationServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReservationNotificationServiceTest {

    @Mock
    private ReservationNotificationRepository reservationNotificationRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationStatusRepository reservationStatusRepository;

    @InjectMocks
    private ReservationNotificationServiceImpl notificationService;

    private ReservationNotification reservationNotification;

    private User user;

    private Reservation reservation;

    private ReservationNotificationDto reservationNotificationDto;

    @BeforeEach
    public void setup(){

        Set<Classroom> classrooms = Set.of(new Classroom());

        user = new User();
        user.setId(1L);

        reservation = new Reservation();
        reservation.setId(2L);
        reservation.setClassrooms(classrooms);

        reservationNotification = new ReservationNotification();
        reservationNotification.setId(1L);
        reservationNotification.setUser(user);
        reservationNotification.setReservation(reservation);


        reservationNotificationDto = ReservaitonNotificationMapper.mapToReservationNotificationDto(reservationNotification);

    }

    public void check(ReservationNotificationDto dto, ReservationNotification notification){
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(notification.getId());
        assertThat(dto.getMessage()).isEqualTo(notification.getMessage());
        assertThat(dto.getReservation().getId()).isEqualTo(notification.getReservation().getId());
        assertThat(dto.getUser().getId()).isEqualTo(notification.getUser().getId());
    }

    @DisplayName("Save reservation notification - should return expected DTO")
    @Test
    public void whenSaveReservationNotification_thenReturnsExpectedDto(){

        ReservationStatus rs = new ReservationStatus();
        rs.setStatus(Status.APPROVED);

        Mockito.when(reservationNotificationRepository.save(Mockito.any(ReservationNotification.class))).thenReturn(reservationNotification);
        Mockito.when(reservationRepository.findById(reservationNotification.getReservation().getId())).thenReturn(Optional.of(reservation));
        Mockito.when(userRepository.findById(reservationNotification.getUser().getId())).thenReturn(Optional.of(user));
        Mockito.when(reservationStatusRepository.findById(reservation.getId())).thenReturn(Optional.of(rs));
        Response<ReservationNotificationDto> response = notificationService.saveReservationNotification(reservationNotificationDto);
        check(response.getDto(), reservationNotification);
        Mockito.verify(reservationNotificationRepository).save(Mockito.any(ReservationNotification.class));


    }

    @DisplayName("Save reservation notification, invalid reservation ID - should throw EntityNotExistingException")
    @Test
    public void invalidReservationId_whenSaveReservationNotification_thenThrowsException(){

        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()->{

            notificationService.saveReservationNotification(reservationNotificationDto);

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not reservation with given ID!");

    }

    @DisplayName("Save reservation notification, invalid user ID - should throw EntityNotExistingException")
    @Test
    public void invalidUserId_whenSaveReservationNotification_thenThrowsException(){

        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()->{

            notificationService.saveReservationNotification(reservationNotificationDto);

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not user with given ID!");

    }

    @DisplayName("Get all reservation notifications - should return list of DTOs")
    @Test
    public void whenGetsAllReservationNotifications_thenReturnsExpectedResponse(){

        ReservationNotification reservationNotification2 = reservationNotification;
        List<ReservationNotification> lista = List.of(reservationNotification,reservationNotification2);
        Mockito.when(reservationNotificationRepository.findAll()).thenReturn(lista);
        Response<List<ReservationNotificationDto>> response = notificationService.getAllReservationNotifications();

        assertThat(response.getDto()).isNotNull();
        assertThat(response.getDto().size()).isEqualTo(2);
        check(response.getDto().get(0), reservationNotification);
        check(response.getDto().get(1),reservationNotification2);



    }

    @DisplayName("Get all reservation notifications - should throw EmptyEntityListException")
    @Test
    public void whenGetsAllReservationNotifications_thenThrowsException(){

        ReservationNotification reservationNotification2 = reservationNotification;
        List<ReservationNotification> lista = List.of(reservationNotification,reservationNotification2);
        Mockito.when(reservationNotificationRepository.findAll()).thenReturn(new ArrayList<>());

        assertThatThrownBy(()->{

            notificationService.getAllReservationNotifications();

        }).isInstanceOf(EmptyEntityListException.class)
                .hasMessageContaining("There are no reservation notifications!");


    }

}
