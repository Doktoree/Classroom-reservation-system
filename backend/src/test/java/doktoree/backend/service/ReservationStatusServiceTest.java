package doktoree.backend.service;

import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.ReservationNotification;
import doktoree.backend.domain.ReservationStatus;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.dtos.ReservationNotificationDto;
import doktoree.backend.dtos.ReservationStatusDto;
import doktoree.backend.enums.Status;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.mapper.ReservaitonNotificationMapper;
import doktoree.backend.mapper.ReservationStatusMapper;
import doktoree.backend.repositories.ReservationRepository;
import doktoree.backend.repositories.ReservationStatusRepository;
import doktoree.backend.repositories.UserRepository;
import doktoree.backend.services.ReservationNotificationServiceImpl;
import doktoree.backend.services.ReservationStatusServiceImpl;
import org.aspectj.lang.annotation.Before;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class ReservationStatusServiceTest {

    @Mock
    private  ReservationStatusRepository reservationStatusRepository;

    @Mock
    private  ReservationRepository reservationRepository;

    @Mock
    private  UserRepository userRepository;

    @Mock
    private  ReservationNotificationServiceImpl reservationNotificationService;

    @InjectMocks
    private ReservationStatusServiceImpl reservationStatusService;

    private ReservationStatus reservationStatus;

    private Reservation reservation;

    private User user;

    private List<ReservationStatus> reservationStatuses;

    private List<Reservation> reservations;

    private ReservationStatus reservationStatus2;

    private ReservationNotificationDto reservationNotificationDto;

    private Response<ReservationNotificationDto> reservationNotificationDtoResponse;

    @BeforeEach
    public void setup(){

        user = new User();
        user.setId(55L);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);

        reservationStatus = new ReservationStatus();
        reservationStatus.setStatus(Status.PENDING);
        reservationStatus.setReservation(reservation);
        reservationStatus.setId(1L);
        reservationStatus2 = reservationStatus;
        reservationStatuses = List.of(reservationStatus, reservationStatus2);

        Reservation reservation2 = reservation;

        reservations = List.of(reservation,reservation2);

        reservationNotificationDto = new ReservationNotificationDto();
        reservationNotificationDto.setReservation(reservation);
        reservationNotificationDto.setId(22L);
        reservationNotificationDto.setUser(user);
        reservationNotificationDto.setMessage("Message 1");

        reservationNotificationDtoResponse = new Response<>();
        reservationNotificationDtoResponse.setDto(reservationNotificationDto);

    }

    public void check(ReservationStatusDto dto, ReservationStatus reservation){
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(reservation.getId());
        assertThat(dto.getRejectingReason()).isEqualTo(reservation.getRejectingReason());
        assertThat(dto.getStatus()).isEqualTo(reservation.getStatus());
        assertThat(dto.getReservation().getId()).isEqualTo(reservation.getReservation().getId());
    }

    @DisplayName("Find reservation status by valid ID - should return expected DTO")
    @Test
    public void whenFindReservationStatusById_thenReturnsExpectedDto(){

        Mockito.when(reservationStatusRepository.findById(reservation.getId())).thenReturn(Optional.of(reservationStatus));
        Response<ReservationStatusDto> response = reservationStatusService.findReservationStatusById(reservation.getId());
        check(response.getDto(), reservationStatus);

    }

    @DisplayName("Find reservation status by invalid ID - should throw EntityNotExistingException")
    @Test
    public void invalidReservationId_whenFindReservationStatusById_thenThrowsException(){

        Mockito.when(reservationStatusRepository.findById(reservation.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            reservationStatusService.findReservationStatusById(reservation.getId());

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is no reservation status with given ID");


    }

    @DisplayName("Save reservation status - should return expected DTO")
    @Test
    public void whenSaveReservationStatus_thenReturnsExpectedDto(){

        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        Mockito.when(reservationStatusRepository.save(Mockito.any(ReservationStatus.class))).thenReturn(reservationStatus);
        Response<ReservationStatusDto> response = reservationStatusService.saveReservationStatus(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));
        check(response.getDto(), reservationStatus);


    }

    @DisplayName("Save reservation status by invalid ID - should throw EntityNotExistingException")
    @Test
    public void invalidReservationId_whenSaveReservationStatus_thenThrowsException(){

        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            reservationStatusService.saveReservationStatus(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not reservation with given ID!");

    }

    @DisplayName("Save reservation status by invalid ID - should throw EntityNotSavedException")
    @Test
    public void whenSaveReservationStatus_thenThrowsException(){

        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        Mockito.when(reservationStatusRepository.save(Mockito.any(ReservationStatus.class))).thenThrow(new EntityNotSavedException("Reservation can not be saved!"));

        assertThatThrownBy(() -> {

            reservationStatusService.saveReservationStatus(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("Reservation can not be saved!");


    }

    @DisplayName("Get all reservation statuses from user by invalid ID - should throw EntityNotExistingException")
    @Test
    public void invalidUserId_whenGetAllReservationStatusFromUser_thenThrowsException(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            reservationStatusService.getAllReservationStatusFromUser(user.getId());

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not user with given ID! ");



    }

    @DisplayName("Get all reservation statuses from user - should return expected DTO")
    @Test
    public void whenGetAllReservationStatusFromUser_thenReturnsExpectedDto(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(reservationRepository.findByUser(user)).thenReturn(reservations);

        reservations.forEach(r -> {

            Mockito.when(reservationStatusRepository.findById(r.getId())).thenReturn(Optional.of(reservationStatus));

        });

        Response<List<ReservationStatusDto>> response = reservationStatusService.getAllReservationStatusFromUser(user.getId());
        check(response.getDto().get(0), reservationStatus);
        check(response.getDto().get(1), reservationStatus2);


    }

    @DisplayName("Get all reservation statuses from user - should throw EmptyEntityListException")
    @Test
    public void whenGetAllReservationStatusFromUser_thenThrowsException(){

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(reservationRepository.findByUser(user)).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> {

            reservationStatusService.getAllReservationStatusFromUser(user.getId());

        }).isInstanceOf(EmptyEntityListException.class)
                .hasMessageContaining("There are not reservations from given user!");

    }

    @DisplayName("Approve reservation - should return expected DTO")
    @Test
    public void whenApproveReservation_thenReturnsExpectedDto(){

        Mockito.when(reservationStatusRepository.findById(reservationStatus.getReservation().getId())).thenReturn(Optional.of(reservationStatus));
        Mockito.when(reservationStatusRepository.save(Mockito.any(ReservationStatus.class))).thenReturn(reservationStatus);
        Mockito.when(reservationRepository.findById(reservationStatus.getReservation().getId())).thenReturn(Optional.of(reservation));

        Mockito.when(reservationNotificationService.saveReservationNotification(Mockito.any())).thenReturn(reservationNotificationDtoResponse);
        Response<ReservationStatusDto> response = reservationStatusService.approveReservation(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));
        ReservationStatusDto dto = response.getDto();
        assertThat(dto.getStatus()).isEqualTo(reservationStatus.getStatus());

    }

    @DisplayName("Approve reservation - should throw EntityNotExistingException")
    @Test
    public void invalidReservationStatusId_whenApproveReservation_thenThrowsException(){

        Mockito.when(reservationStatusRepository.findById(reservationStatus.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            reservationStatusService.approveReservation(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is no reservation status with given ID!");

    }

    @DisplayName("Approve reservation - should throw EntityNotExistingException")
    @Test
    public void invalidReservationId_whenApproveReservation_thenThrowsException(){

        Mockito.when(reservationStatusRepository.findById(reservationStatus.getId())).thenReturn(Optional.of(reservationStatus));
        Mockito.when(reservationRepository.findById(reservationStatus.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            reservationStatusService.approveReservation(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is no reservation with given ID!");

    }

    @DisplayName("Approve reservation - should throw EntityNotSavedException")
    @Test
    public void whenApproveReservation_thenThrowsException(){

        Mockito.when(reservationStatusRepository.findById(reservationStatus.getId())).thenReturn(Optional.of(reservationStatus));
        Mockito.when(reservationRepository.findById(reservationStatus.getId())).thenReturn(Optional.of(reservation));
        Mockito.when(reservationStatusRepository.save(reservationStatus)).thenThrow(new EntityNotSavedException("Reservation status can not be saved!"));
        assertThatThrownBy(() -> {

            reservationStatusService.approveReservation(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("Reservation status can not be saved!");

    }

    @DisplayName("Approve reservation - should throw EntityNotSavedException")
    @Test
    public void whenApproveReservation_thenThrowsException2(){

        Mockito.when(reservationStatusRepository.findById(reservationStatus.getId())).thenReturn(Optional.of(reservationStatus));
        Mockito.when(reservationRepository.findById(reservationStatus.getId())).thenReturn(Optional.of(reservation));
        Mockito.when(reservationStatusRepository.save(reservationStatus)).thenReturn(reservationStatus);
        Mockito.when(reservationNotificationService.saveReservationNotification(reservationNotificationDto)).thenThrow(new EntityNotSavedException("Reservation status can not be saved!"));
        assertThatThrownBy(() -> {

            reservationStatusService.approveReservation(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("Reservation status can not be saved!");

    }

    @DisplayName("Reject reservation - should return expected DTO")
    @Test
    public void whenRejectReservation_thenReturnsExpectedDto(){

        Mockito.when(reservationStatusRepository.findById(reservationStatus.getReservation().getId())).thenReturn(Optional.of(reservationStatus));
        Mockito.when(reservationStatusRepository.save(Mockito.any(ReservationStatus.class))).thenReturn(reservationStatus);
        Mockito.when(reservationRepository.findById(reservationStatus.getReservation().getId())).thenReturn(Optional.of(reservation));

        Mockito.when(reservationNotificationService.saveReservationNotification(Mockito.any())).thenReturn(reservationNotificationDtoResponse);
        Response<ReservationStatusDto> response = reservationStatusService.rejectReservation(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));
        ReservationStatusDto dto = response.getDto();
        assertThat(dto.getStatus()).isEqualTo(reservationStatus.getStatus());

    }

    @DisplayName("Reject reservation - should throw EntityNotExistingException")
    @Test
    public void invalidReservationStatusId_whenRejectReservation_thenThrowsException(){

        Mockito.when(reservationStatusRepository.findById(reservationStatus.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            reservationStatusService.rejectReservation(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is no reservation status with given ID!");

    }

    @DisplayName("Reject reservation - should throw EntityNotExistingException")
    @Test
    public void invalidReservationId_whenRejectReservation_thenThrowsException(){

        Mockito.when(reservationStatusRepository.findById(reservationStatus.getId())).thenReturn(Optional.of(reservationStatus));
        Mockito.when(reservationRepository.findById(reservationStatus.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {

            reservationStatusService.rejectReservation(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not reservation with given ID!");

    }

    @DisplayName("Reject reservation - should throw EntityNotSavedException")
    @Test
    public void whenRejectReservation_thenThrowsException(){

        Mockito.when(reservationStatusRepository.findById(reservationStatus.getId())).thenReturn(Optional.of(reservationStatus));
        Mockito.when(reservationRepository.findById(reservationStatus.getId())).thenReturn(Optional.of(reservation));
        Mockito.when(reservationStatusRepository.save(reservationStatus)).thenThrow(new EntityNotSavedException("Reservation status can not be saved!"));
        assertThatThrownBy(() -> {

            reservationStatusService.rejectReservation(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("Reservation status can not be saved!");

    }

    @DisplayName("Approve reservation - should throw EntityNotSavedException")
    @Test
    public void whenRejectReservation_thenThrowsException2(){

        Mockito.when(reservationStatusRepository.findById(reservationStatus.getId())).thenReturn(Optional.of(reservationStatus));
        Mockito.when(reservationRepository.findById(reservationStatus.getId())).thenReturn(Optional.of(reservation));
        Mockito.when(reservationStatusRepository.save(reservationStatus)).thenReturn(reservationStatus);
        Mockito.when(reservationNotificationService.saveReservationNotification(reservationNotificationDto)).thenThrow(new EntityNotSavedException("Reservation status can not be saved!"));
        assertThatThrownBy(() -> {

            reservationStatusService.rejectReservation(ReservationStatusMapper.mapToReservationStatusDto(reservationStatus));

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("Reservation status can not be saved!");

    }


}
