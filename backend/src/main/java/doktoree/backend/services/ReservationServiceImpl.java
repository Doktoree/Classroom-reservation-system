package doktoree.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doktoree.backend.domain.Reservation;
import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.factory.ReservationFactory;
import doktoree.backend.mapper.ReservationMapper;
import doktoree.backend.repositories.ReservationRepository;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Override
	public Response<ReservationDto> findReservationById(Long id) throws EntityNotExistingException {
		
		Optional<Reservation> optioanalReservation = reservationRepository.findById(id);
		Response<ReservationDto> response = new Response<>();
		
		if(optioanalReservation.isEmpty())
			throw new EntityNotExistingException("There is not reservation with given ID!");
		
		Reservation reservation = optioanalReservation.get();
		ReservationDto dto = ReservationMapper.mapToReservationDto(reservation);
		response.setDto(dto);
		response.setMessage("Reservation successfully found!");
		
		return response;
	}

	@Override
	public Response<ReservationDto> saveReservation(ReservationDto dto) throws EntityNotExistingException {
		
		Reservation reservation = ReservationFactory.createReservation(dto);
		Response<ReservationDto> response = new Response<>();
		
		try {
			Reservation savedReservation = reservationRepository.save(reservation);
			ReservationDto savedReservationDto = ReservationMapper.mapToReservationDto(savedReservation);
			response.setDto(savedReservationDto);
			response.setMessage("Reservation successfully saved!");
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException("Reservation can not be saved! "  + e.getMessage());
		}
		
	}

	@Override
	public Response<ReservationDto> deleteReservation(Long id) throws EntityNotExistingException, EntityNotExistingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<List<ReservationDto>> getAllReservations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<ReservationDto> updateReservation(ReservationDto dto) throws EntityNotExistingException, EntityNotSavedException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
