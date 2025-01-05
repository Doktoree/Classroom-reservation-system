package doktoree.backend.mapper;

import doktoree.backend.domain.ReservationStatus;
import doktoree.backend.dtos.ReservationStatusDto;

public class ReservationStatusMapper {

	public static ReservationStatus mapToReservationStatus(ReservationStatusDto dto) {
		
		return new ReservationStatus(dto.getId(),dto.getReservation(), dto.getStatus(), dto.getRejectingReason());
	}
	
	public static ReservationStatusDto mapToReservationStatusDto(ReservationStatus reservationStatus) {
		
		return new ReservationStatusDto(reservationStatus.getId(),reservationStatus.getReservation(), reservationStatus.getStatus(), reservationStatus.getRejectingReason());
	}
	
	
}
