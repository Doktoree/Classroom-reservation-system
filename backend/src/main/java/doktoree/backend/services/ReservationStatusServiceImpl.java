package doktoree.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import doktoree.backend.exceptions.EmptyEntityListException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.ReservationNotification;
import doktoree.backend.domain.ReservationStatus;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.ReservationNotificationDto;
import doktoree.backend.dtos.ReservationStatusDto;
import doktoree.backend.enums.Status;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.mapper.ReservationNotificationMapper;
import doktoree.backend.mapper.ReservationStatusMapper;
import doktoree.backend.repositories.ReservationRepository;
import doktoree.backend.repositories.ReservationStatusRepository;
import doktoree.backend.repositories.UserRepository;

@Service
public class ReservationStatusServiceImpl implements ReservationStatusService {

	private final ReservationStatusRepository reservationStatusRepository;
	
	private final ReservationRepository reservationRepository;
	
	private final UserRepository userRepository;
	
	private final ReservationNotificationServiceImpl reservationNotificationService;
	
	
	
	@Autowired
	public ReservationStatusServiceImpl(ReservationStatusRepository reservationStatusRepository,
			ReservationRepository reservationRepository, UserRepository userRepository,
			ReservationNotificationServiceImpl reservationNotificationService) {
		this.reservationStatusRepository = reservationStatusRepository;
		this.reservationRepository = reservationRepository;
		this.userRepository = userRepository;
		this.reservationNotificationService = reservationNotificationService;
	}

	@Override
	public Response<ReservationStatusDto> findReservationStatusById(Long id)
			throws EntityNotExistingException {

		Optional<ReservationStatus> optionalReservationStatus = reservationStatusRepository
				.findById(id);
		
		if (optionalReservationStatus.isEmpty()) {
			throw new EntityNotExistingException(
					"There is no reservation status with given ID"
			);
		}

		ReservationStatusDto dto = ReservationStatusMapper
				.mapToReservationStatusDto(optionalReservationStatus.get());
		Response<ReservationStatusDto> response = new Response<>();
		response.setDtoT(dto);
		response.setMessage("Reservation status found successfully!");
		
		return response;
	}

	@Override
	public Response<ReservationStatusDto> saveReservationStatus(ReservationStatusDto dto)
			throws EntityNotSavedException, EntityNotExistingException {
		
		Reservation reservation = reservationRepository
				.findById(dto.getReservation().getId())
				.orElseThrow(() -> new EntityNotExistingException(
						"There is not reservation with given ID!"
				));
		ReservationStatus reservationStatus = ReservationStatusMapper
				.mapToReservationStatus(dto);
		reservationStatus.setReservation(reservation);
		
		try {
			ReservationStatus savedReservationStatus = reservationStatusRepository
					.save(reservationStatus);
			ReservationStatusDto savedDto = ReservationStatusMapper
					.mapToReservationStatusDto(savedReservationStatus);
			Response<ReservationStatusDto> response = new Response<>();
			response.setDtoT(savedDto);
			response.setMessage("Reservation status successfully saved!");
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException("Reservation can not be saved!");
		}
		
	}

	@Override
	public Response<List<ReservationStatusDto>> getAllReservationStatusFromUser(Long userId) {
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotExistingException(
						"There is not user with given ID! "
				));
		
		List<Reservation> reservationsByUser = reservationRepository
				.findByUser(user);

		if (reservationsByUser.isEmpty()) {
			throw new EmptyEntityListException(
					"There are not reservations from given user!"
			);
		}

		List<ReservationStatus> listOfReservationStatus = new ArrayList<>();

		reservationsByUser.forEach(r -> {

			Optional<ReservationStatus> rs = reservationStatusRepository
					.findById(r.getId());
            rs.ifPresent(listOfReservationStatus::add);

		});
		
		List<ReservationStatusDto> reservationStatusDtos = listOfReservationStatus.stream()
				.map(ReservationStatusMapper::mapToReservationStatusDto)
				.collect(Collectors.toList());
		Response<List<ReservationStatusDto>> response = new Response<>();
		response.setDtoT(reservationStatusDtos);
		response.setMessage("All reservation statuses are successfully found!");
		
		return response;
	}

	@Override
	public Response<ReservationStatusDto> approveReservation(ReservationStatusDto dto)
			throws EntityNotExistingException, EntityNotSavedException {
		
		ReservationStatus rs = reservationStatusRepository
				.findById(dto.getReservation().getId())
				.orElseThrow(() -> new EntityNotExistingException(
						"There is no reservation status with given ID!"
				));

		rs.setStatus(Status.APPROVED);
		rs.setRejectingReason(null);
		Reservation reservation = reservationRepository
				.findById(rs.getId())
				.orElseThrow(() -> new EntityNotExistingException(
						"There is no reservation with given ID!"
				));

		try {
			reservationStatusRepository.save(rs);
			ReservationStatusDto reservationStatusDto = ReservationStatusMapper
					.mapToReservationStatusDto(rs);

			ReservationNotification rn = new ReservationNotification(
					null,
					null,
					reservation,
					reservation.getUser());

			ReservationNotificationDto rnDto = ReservationNotificationMapper
					.mapToReservationNotificationDto(rn);
			String message = reservationNotificationService
					.saveReservationNotification(rnDto).getMessage();
			Response<ReservationStatusDto> response = new Response<>();
			response.setDtoT(reservationStatusDto);
			response.setMessage(message);
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException(
					"Reservation status can not be saved!"
			);
		}
		
	}

	@Override
	public Response<ReservationStatusDto> rejectReservation(ReservationStatusDto dto)
			throws EntityNotExistingException, EntityNotSavedException {
		ReservationStatus rs = reservationStatusRepository
				.findById(dto.getReservation().getId())
				.orElseThrow(() -> new EntityNotExistingException(
						"There is no reservation status with given ID!"
				));

		rs.setStatus(Status.REJECTED);
		rs.setRejectingReason(dto.getRejectingReason());
		Reservation reservation = reservationRepository
				.findById(rs.getId())
				.orElseThrow(() -> new EntityNotExistingException(
						"There is not reservation with given ID!"
				));
		try {
			reservationStatusRepository.save(rs);
			ReservationStatusDto reservationStatusDto = ReservationStatusMapper
					.mapToReservationStatusDto(rs);
			ReservationNotification rn = new ReservationNotification(
					null,
					null,
					reservation,
					reservation.getUser());

			ReservationNotificationDto rnDto = ReservationNotificationMapper
					.mapToReservationNotificationDto(rn);
			String message = reservationNotificationService
					.saveReservationNotification(rnDto)
					.getMessage();
			Response<ReservationStatusDto> response = new Response<>();
			response.setDtoT(reservationStatusDto);
			response.setMessage(message);
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException(
					"Reservation status can not be saved!"
			);
		}
	}


}
