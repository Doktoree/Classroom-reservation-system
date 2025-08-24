package doktoree.backend.mapper;

import doktoree.backend.domain.ReservationStatus;
import doktoree.backend.dtos.ReservationStatusDto;
import doktoree.backend.factory.ReservationFactory;

public class ReservationStatusMapper {

	public static ReservationStatus mapToReservationStatus(ReservationStatusDto dto) {
		
		return new ReservationStatus(
				dto.getId(),
				ReservationFactory.createReservation(dto.getReservationDto()),
				dto.getStatus(),
				dto.getRejectingReason()
		);
	}
	
	public static ReservationStatusDto mapToReservationStatusDto(
			ReservationStatus reservationStatus
	) {
		
		return new ReservationStatusDto(
				reservationStatus.getId(),
				ReservationMapper.mapToReservationDto(reservationStatus.getReservation()),
				reservationStatus.getStatus(),
				reservationStatus.getRejectingReason()
		);
	}
	
	
}
