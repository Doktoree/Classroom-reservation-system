package doktoree.backend.services;

import java.util.List;

import doktoree.backend.dtos.ReservationStatusDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;

public interface ReservationStatusService {

	
	public Response<ReservationStatusDto> findReservationStatusById(Long id) throws EntityNotExistingException;

	public Response<ReservationStatusDto> saveReservationStatus(ReservationStatusDto dto) throws EntityNotSavedException, EntityNotExistingException;

	public Response<List<ReservationStatusDto>> getAllReservationStatusFromUser(Long userId);

	public Response<ReservationStatusDto> approveReservation(ReservationStatusDto dto) throws EntityNotExistingException, EntityNotSavedException;
	
	public Response<ReservationStatusDto> rejectReservation(ReservationStatusDto dto) throws EntityNotExistingException, EntityNotSavedException;
}
