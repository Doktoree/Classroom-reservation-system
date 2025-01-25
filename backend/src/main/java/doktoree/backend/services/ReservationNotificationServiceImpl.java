package doktoree.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

	public String saveReservationNotification(ReservationNotificationDto dto) throws EntityNotSavedException {

		Reservation reservation = reservationRepository.findById(dto.getReservation().getId()).orElseThrow(() -> new EntityNotExistingException("There is not reservation with given ID!"));
		User user = userRepository.findById(dto.getUser().getId()).orElseThrow(() -> new EntityNotExistingException("There is not user with given ID!"));
		Optional<ReservationStatus> reservationStatus = reservationStatusRepository.findById(reservation.getId());
		String status = "";
		
		if(reservationStatus.get().getStatus() == Status.APPROVED)
			status = "approved";
		else if(reservationStatus.get().getStatus() == Status.REJECTED)
			status = "rejected";
		Set<Classroom> classrooms = reservation.getClassrooms();
		String classroomsMessage = "";
		
		for(Classroom c: classrooms) {
			
			classroomsMessage+=c.getClassRoomNumber() + " ";			
		}
		
		String message = "Reservation number" + reservation.getId() 
		+ " in classrooms "  + classroomsMessage 
		+ ", scheduled on " + reservation.getDate() + " , starting at " + reservation.getStartTime() 
		+ " and ending at " + reservation.getEndTime() + ", has been " + status;
		
		try {
			ReservationNotification reservationNotification = new ReservationNotification(null, message, reservation, user);
			ReservationNotification savedReservationNotification = reservationNotificationRepository.save(reservationNotification);
			return savedReservationNotification.getMessage();
		} catch (Exception e) {
			throw new EntityNotSavedException("Reservation notification can not be saved! " + e.getMessage());
		}
		
	}

	@Override
	public Response<List<ReservationNotificationDto>> getAllReservationNotifications() {
		
		List<ReservationNotification> reservationNotifications = reservationNotificationRepository.findAll();
		List<ReservationNotificationDto> notificationDtos = reservationNotifications.stream().map(ReservaitonNotificationMapper::mapToReservationNotificationDto).collect(Collectors.toList());
		Response<List<ReservationNotificationDto>> response = new Response<>();
		response.setDto(notificationDtos);
		response.setMessage("All reservation notifications are successfully found!");
		return response;
		
	}

	@Override
	public Response<List<ReservationNotificationDto>> getAllReservatioNotificationsPending() {
		return null;
	}

	

}
