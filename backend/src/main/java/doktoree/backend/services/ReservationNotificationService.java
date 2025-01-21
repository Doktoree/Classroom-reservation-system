package doktoree.backend.services;

import java.util.List;

import doktoree.backend.dtos.ReservationNotificationDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EntityNotSavedException;

public interface ReservationNotificationService {

	public String saveReservationNotification(ReservationNotificationDto dto) throws EntityNotSavedException;
	
	public Response<List<ReservationNotificationDto>> getAllReservationNotifications();
	
	public Response<List<ReservationNotificationDto>> getAllReservatioNotificationsPending();
}
