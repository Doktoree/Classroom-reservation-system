package doktoree.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doktoree.backend.domain.Classroom;
import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.ReservationStatus;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.dtos.ReservationStatusDto;
import doktoree.backend.enums.Status;
import doktoree.backend.error_response.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.exceptions.InvalidForeignKeyException;
import doktoree.backend.factory.ReservationFactory;
import doktoree.backend.mapper.ReservationMapper;
import doktoree.backend.mapper.ReservationStatusMapper;
import doktoree.backend.repositories.ClassroomRepository;
import doktoree.backend.repositories.ReservationRepository;
import doktoree.backend.repositories.UserRepository;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ClassroomRepository classroomRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReservationStatusServiceImpl reservationStatusService;
	
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
		
		//napraviti posebnu klasu za proveru kljuceva
		Set<Classroom> classrooms = dto.getClassrooms();
		for(Classroom c: classrooms) {		
			classroomRepository.findById(c.getId()).orElseThrow(()-> new InvalidForeignKeyException("There is no classroom with given ID!"));			
		}
		userRepository.findById(dto.getUser().getId()).orElseThrow(()-> new InvalidForeignKeyException("There is no user with given ID!"));
		
		try {
			Reservation savedReservation = reservationRepository.save(reservation);
			ReservationDto savedReservationDto = ReservationMapper.mapToReservationDto(savedReservation);
			ReservationStatus reservationStatus = new ReservationStatus(null, savedReservation, Status.PENDING, null);
			ReservationStatusDto reservationStatusDto = ReservationStatusMapper.mapToReservationStatusDto(reservationStatus);
			reservationStatusService.saveReservationStatus(reservationStatusDto);
			response.setDto(savedReservationDto);
			response.setMessage("Reservation successfully saved!");
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException("Reservation can not be saved! "  + e.getMessage());
		}
		
	}

	@Override
	public Response<ReservationDto> deleteReservation(Long id) throws EntityNotExistingException, EntityNotExistingException {
		// Razmisliti
		return null;
	}

	@Override
	public Response<List<ReservationDto>> getAllReservations() {
		
		List<ReservationDto> reservationDtos = new ArrayList<>();
		List<Reservation> reservations = reservationRepository.findAll();
		Response<List<ReservationDto>> response = new Response<>();
		
		if(reservations.isEmpty())
			throw new EmptyEntityListException("There are no reservations!");
		
		reservationDtos = reservations.stream().map(ReservationMapper::mapToReservationDto).collect(Collectors.toList());
		response.setDto(reservationDtos);
		response.setMessage("All reservations successfully found!");
		
		return response;
		
		
	}

	@Override
	public Response<ReservationDto> updateReservation(ReservationDto dto) throws EntityNotExistingException, EntityNotSavedException {
		
		Optional<Reservation> optionalReservation = reservationRepository.findById(dto.getId());
		
		if(optionalReservation.isEmpty())
			throw new EntityNotExistingException("There is not reservation with given ID!");
		
		Set<Classroom> classrooms = dto.getClassrooms();
		for(Classroom c: classrooms) {		
			classroomRepository.findById(c.getId()).orElseThrow(()-> new InvalidForeignKeyException("There is no classroom with given ID!"));			
		}
		userRepository.findById(dto.getUser().getId()).orElseThrow(()-> new InvalidForeignKeyException("There is no user with given ID!"));
		
		try {
			Reservation reservation = ReservationFactory.createReservation(dto);
			Reservation updatedReservation = reservationRepository.save(reservation);
			ReservationDto updatedReseravtionDto = ReservationMapper.mapToReservationDto(updatedReservation);
			
			Response<ReservationDto> response = new Response<>();
			response.setDto(updatedReseravtionDto);
			response.setMessage("Reservation is successfully updated!");
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException("Reservation can not be updated! "  + e.getMessage());
		}
		
		
	}

	@Override
	public Response<List<ReservationDto>> getAllReservationsFromUser(Long userId) {
		
		User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotExistingException("There is no user with given ID"));
		List<Reservation> reservations = reservationRepository.findByUser(user);
		
		if(reservations.isEmpty())
			throw new EmptyEntityListException("List of reservation is empty!");
		
		List<ReservationDto> reservationDtos = reservations.stream().map(ReservationMapper::mapToReservationDto).collect(Collectors.toList());
		Response<List<ReservationDto>> response = new Response<>();
		response.setDto(reservationDtos);
		response.setMessage("All reservations are successfully found!");
		return response;

	}
	
	
}
