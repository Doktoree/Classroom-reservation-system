package doktoree.backend.services;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import doktoree.backend.dtos.ClassroomDto;
import doktoree.backend.mapper.ClassroomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import doktoree.backend.domain.Classroom;
import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.ReservationNotification;
import doktoree.backend.domain.ReservationStatus;
import doktoree.backend.domain.User;
import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.dtos.ReservationStatusDto;
import doktoree.backend.enums.Status;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.exceptions.InvalidForeignKeyException;
import doktoree.backend.factory.ReservationFactory;
import doktoree.backend.mapper.ReservationMapper;
import doktoree.backend.mapper.ReservationStatusMapper;
import doktoree.backend.repositories.ClassroomRepository;
import doktoree.backend.repositories.ReservationNotificationRepository;
import doktoree.backend.repositories.ReservationRepository;
import doktoree.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;

	private final ClassroomRepository classroomRepository;
	
	private final UserRepository userRepository;
	
	private final ReservationStatusServiceImpl reservationStatusService;
	
	private final ReservationNotificationRepository notificationRepository;

	private final MailService mailService;
	
	@Autowired
	public ReservationServiceImpl(ReservationRepository resRepo, ClassroomRepository classRepo,
			UserRepository userRepo, ReservationStatusServiceImpl resStatusService,
			ReservationNotificationRepository notRepo,
			MailService mailService) {
		this.reservationRepository = resRepo;
		this.classroomRepository = classRepo;
		this.userRepository = userRepo;
		this.reservationStatusService = resStatusService;
		this.notificationRepository = notRepo;
		this.mailService = mailService;
	}

	@Override
	public Response<ReservationDto> findReservationById(Long id)
			throws EntityNotExistingException {
		
		Optional<Reservation> optionalReservation = reservationRepository.findById(id);
		Response<ReservationDto> response = new Response<>();
		
		if (optionalReservation.isEmpty()) {
			throw new EntityNotExistingException(
					"There is not reservation with given ID!"
			);
		}

		
		Reservation reservation = optionalReservation.get();
		ReservationDto dto = ReservationMapper.mapToReservationDto(reservation);
		response.setDtoT(dto);
		response.setMessage("Reservation successfully found!");
		
		return response;
	}

	@Transactional
	@Override
	public Response<ReservationDto> saveReservation(ReservationDto dto)
			throws EntityNotSavedException {
		
		Reservation reservation = ReservationFactory.createReservation(dto);
		Response<ReservationDto> response = new Response<>();
		
		Set<Classroom> classrooms = dto.getClassrooms();

		classrooms.forEach((c) -> {

			classroomRepository.findById(c.getId())
					.orElseThrow(() -> new InvalidForeignKeyException(
							"There is no classroom with given ID!"
					)
			);

		});

		userRepository.findById(
				dto.getUser().getId())
				.orElseThrow(() -> new InvalidForeignKeyException(
						"There is no user with given ID!"
				)
		);
		
		try {
			Reservation savedReservation = reservationRepository.save(reservation);
			ReservationDto savedReservationDto = ReservationMapper
					.mapToReservationDto(savedReservation);
			ReservationStatus reservationStatus = new ReservationStatus(
					null,
					savedReservation,
					Status.PENDING,
					null);
			ReservationStatusDto reservationStatusDto = ReservationStatusMapper
					.mapToReservationStatusDto(reservationStatus);
			reservationStatusService.saveReservationStatus(reservationStatusDto);
			response.setDtoT(savedReservationDto);
			response.setMessage("Reservation successfully saved!");
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException("Reservation can not be saved!");
		}
		
	}

	@Override
	public Response<List<ReservationDto>> getAllReservations(int pageNumber)
			throws EmptyEntityListException {
		
		List<ReservationDto> reservationDtos;
		Page<Reservation> reservationsPage = reservationRepository
				.findAll(PageRequest.of(pageNumber, 10));
		List<Reservation> reservations = reservationsPage.getContent();
		Response<List<ReservationDto>> response = new Response<>();
		
		if (reservations.isEmpty()) {
			throw new EmptyEntityListException("There are no reservations!");
		}

		reservationDtos = reservations.stream()
				.map(ReservationMapper::mapToReservationDto)
				.collect(Collectors.toList());
		response.setDtoT(reservationDtos);
		response.setMessage("All reservations successfully found!");
		
		return response;
				
	}

	@Override
	@Transactional
	public Response<ReservationDto> updateReservation(ReservationDto dto)
			throws EntityNotExistingException, EntityNotSavedException {
		
		Optional<Reservation> optionalReservation = reservationRepository
				.findById(dto.getId());
		
		if (optionalReservation.isEmpty()) {
			throw new EntityNotExistingException(
					"There is not reservation with given ID!"
			);
		}

		Reservation exOptionalReservation = optionalReservation.get();
		exOptionalReservation.getClassrooms().size();


		Set<Classroom> classrooms = dto.getClassrooms();

		classrooms.forEach((c) -> {
			classroomRepository.findById(c.getId())
					.orElseThrow(() -> new InvalidForeignKeyException(
							"There is no classroom with given ID!"
					));
		});

		userRepository.findById(
				dto.getUser().getId())
				.orElseThrow(() -> new InvalidForeignKeyException(
						"There is no user with given ID!"
				)
		);
		
		try {
			Reservation reservation = ReservationFactory.createReservation(dto);
			Reservation updatedReservation = reservationRepository.save(reservation);
			ReservationNotification rn = new ReservationNotification(
					null,
					"Some of classrooms have been changed!",
					updatedReservation,
					updatedReservation.getUser());
			ReservationNotification savedRn = notificationRepository
					.save(rn);
			if(!classrooms.equals(exOptionalReservation.getClassrooms())) {
				mailService.sendEmailChangeClassrooms(savedRn);
			}
			ReservationDto updatedReservationDto = ReservationMapper
					.mapToReservationDto(updatedReservation);
			Response<ReservationDto> response = new Response<>();
			response.setDtoT(updatedReservationDto);
			response.setMessage("Reservation is successfully updated!");
			return response;
		} catch (Exception e) {
			throw new EntityNotSavedException("Reservation can not be updated!");
		}
		
		
	}

	@Override
	public Response<List<ReservationDto>> getAllReservationsFromUser(
			Long userId, int pageNumber) throws EmptyEntityListException {
		
		User user = userRepository
				.findById(userId).orElseThrow(() -> new EntityNotExistingException(
						"There is no user with given ID"
				));
		Page<Reservation> reservationsPage = reservationRepository
				.findByUser(user, PageRequest.of(pageNumber, 10));
		
		List<Reservation> reservations = reservationsPage.getContent();
		
		if (reservations.isEmpty()) {
			throw new EmptyEntityListException("List of reservation is empty!");
		}

		List<ReservationDto> reservationDtos = reservations.stream()
				.map(ReservationMapper::mapToReservationDto)
				.collect(Collectors.toList());
		Response<List<ReservationDto>> response = new Response<>();
		response.setDtoT(reservationDtos);
		response.setMessage("All reservations are successfully found!");
		return response;

	}

	@Override
	public Response<List<ClassroomDto>> getAllAvailableClassrooms(ReservationDto dto)
			throws EmptyEntityListException {

			List<Reservation> reservations = reservationRepository
					.findByDateAndStartTimeAfterAndEndTimeBefore(
							dto.getDate(),
							dto.getStartTime(),
							dto.getEndTime()
					);

			Set<Classroom> classrooms = new HashSet<>();

			reservations.forEach(r -> {

				r.getClassrooms().forEach(c -> {

					Optional<Classroom> optionalClassroom = classroomRepository
							.findById(c.getId());
					classrooms.add(optionalClassroom.get());

				});

			});

			List<Classroom> allClassrooms = classroomRepository.findAll();
			allClassrooms.removeAll(classrooms);

			if (allClassrooms.isEmpty()) {

				throw new EmptyEntityListException(
						"There are no available classrooms!"
				);

			}



		List<ClassroomDto> classroomDtos = allClassrooms.stream()
				.map(ClassroomMapper::mapToClassroomDto).toList();
		Response<List<ClassroomDto>> response = new Response<>();
		response.setDtoT(classroomDtos);
		response.setMessage("All available classrooms are successfully found!");
		return response;
	}


}
