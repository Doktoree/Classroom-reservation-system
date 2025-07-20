package doktoree.backend.mapper;

import doktoree.backend.domain.ReservationNotification;
import doktoree.backend.dtos.ReservationNotificationDto;

public class ReservaitonNotificationMapper {

	
	public static ReservationNotification mapToReservationNotification(ReservationNotificationDto dto) {
		
		
		return new ReservationNotification(dto.getId(), dto.getMessage(), dto.getReservation(), dto.getUser());
		
	}
	
	public static ReservationNotificationDto mapToReservationNotificationDto(ReservationNotification rn) {
		
		
		return new ReservationNotificationDto(rn.getId(), rn.getMessage(), rn.getReservation(), rn.getUser());
		
	}
	
}
