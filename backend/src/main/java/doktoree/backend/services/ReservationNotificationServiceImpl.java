package doktoree.backend.services;

import java.util.*;
import java.util.stream.Collectors;

import doktoree.backend.exceptions.EmptyEntityListException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doktoree.backend.domain.Classroom;
import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.ReservationNotification;
import doktoree.backend.domain.ReservationStatus;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.ReservationNotificationDto;
import doktoree.backend.enums.Status;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.mapper.ReservaitonNotificationMapper;
import doktoree.backend.repositories.ReservationNotificationRepository;
import doktoree.backend.repositories.ReservationRepository;
import doktoree.backend.repositories.ReservationStatusRepository;
import doktoree.backend.repositories.UserRepository;

@Service
public class ReservationNotificationServiceImpl implements ReservationNotificationService{

	
	private final ReservationNotificationRepository reservationNotificationRepository;
	
	private final ReservationRepository reservationRepository;
	
	private final UserRepository userRepository;
	
	private final ReservationStatusRepository reservationStatusRepository;
	
	@Autowired
	public ReservationNotificationServiceImpl(ReservationNotificationRepository reservationNotificationRepository,
			ReservationRepository reservationRepository, UserRepository userRepository,
			ReservationStatusRepository reservationStatusRepository) {
		super();
		this.reservationNotificationRepository = reservationNotificationRepository;
		this.reservationRepository = reservationRepository;
		this.userRepository = userRepository;
		this.reservationStatusRepository = reservationStatusRepository;
	}

	public Response<ReservationNotificationDto> saveReservationNotification(ReservationNotificationDto dto) throws EntityNotSavedException,EntityNotExistingException {

		Reservation reservation = reservationRepository.findById(dto.getReservation().getId()).orElseThrow(() -> new EntityNotExistingException("There is not reservation with given ID!"));
		User user = userRepository.findById(dto.getUser().getId()).orElseThrow(() -> new EntityNotExistingException("There is not user with given ID!"));
		Optional<ReservationStatus> reservationStatus = reservationStatusRepository.findById(reservation.getId());

		String status = switch (reservationStatus.get().getStatus()){
			case REJECTED -> "rejected";
			case APPROVED -> "approved";
			default -> "pending";
		};

		Set<Classroom> classrooms = reservation.getClassrooms();

		String classroomsMessage = classrooms.stream()
				.map(Classroom::getClassRoomNumber)
				.map(String::valueOf)
				.collect(Collectors.joining(", "));
		
		String message = "Reservation number" + reservation.getId() 
		+ " in classrooms "  + classroomsMessage
		+ ", scheduled on " + reservation.getDate() + " , starting at " + reservation.getStartTime() 
		+ " and ending at " + reservation.getEndTime() + ", has been " + status;
		
		try {
			ReservationNotification reservationNotification = new ReservationNotification(null, message, reservation, user);
			ReservationNotification savedReservationNotification = reservationNotificationRepository.save(reservationNotification);
			Response<ReservationNotificationDto> response = new Response<>();
			response.setDto(ReservaitonNotificationMapper.mapToReservationNotificationDto(savedReservationNotification));
			response.setMessage("Reservation notification successfully saved!");
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException("Reservation notification can not be saved! " + e.getMessage());
		}
		
	}

	@Override
	public Response<List<ReservationNotificationDto>> getAllReservationNotifications() throws EmptyEntityListException {
		
		List<ReservationNotification> reservationNotifications = reservationNotificationRepository.findAll();

		if(reservationNotifications.isEmpty())
			throw new EmptyEntityListException("There are no reservation notifications!");

		List<ReservationNotificationDto> notificationDtos = reservationNotifications.stream().map(ReservaitonNotificationMapper::mapToReservationNotificationDto).collect(Collectors.toList());
		Response<List<ReservationNotificationDto>> response = new Response<>();
		response.setDto(notificationDtos);
		response.setMessage("All reservation notifications are successfully found!");
		return response;
		
	}



	

}
