package doktoree.backend.services;

import java.util.List;

import doktoree.backend.dtos.ClassroomDto;
import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;


public interface ReservationService {

	public Response<ReservationDto> findReservationById(Long id) throws EntityNotExistingException;
	
	public Response<ReservationDto> saveReservation(ReservationDto dto) throws EntityNotExistingException;
	
	public Response<ReservationDto> deleteReservation(Long id) throws EntityNotExistingException, EntityNotExistingException;
	
	public Response<List<ReservationDto>> getAllReservations(int pageNumber) throws EmptyEntityListException;
	
	public Response<ReservationDto> updateReservation(ReservationDto dto) throws EntityNotExistingException, EntityNotSavedException;
	
	public Response<List<ReservationDto>> getAllReservationsFromUser(Long userId, int pageNumber) throws EmptyEntityListException;

	public Response<List<ClassroomDto>> getAllAvailableClassrooms(ReservationDto dto) throws EmptyEntityListException;
}
