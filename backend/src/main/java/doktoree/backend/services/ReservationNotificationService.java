package doktoree.backend.services;

import java.util.List;
import java.util.Map;

import doktoree.backend.domain.ReservationNotification;
import doktoree.backend.dtos.ReservationNotificationDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;

public interface ReservationNotificationService {

	public Response<ReservationNotificationDto> saveReservationNotification(ReservationNotificationDto dto) throws EntityNotSavedException, EntityNotExistingException;
	
	public Response<List<ReservationNotificationDto>> getAllReservationNotifications() throws EmptyEntityListException;

}
