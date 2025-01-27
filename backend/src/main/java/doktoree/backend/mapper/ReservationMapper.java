package doktoree.backend.mapper;

import doktoree.backend.domain.ColloquiumReservation;
import doktoree.backend.domain.CouncilReservation;
import doktoree.backend.domain.CourseReservation;
import doktoree.backend.domain.DepartmentMeetingReservation;
import doktoree.backend.domain.ExamReservation;
import doktoree.backend.domain.OtherMeetingReservation;
import doktoree.backend.domain.OtherWorkshopReservation;
import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.StudentOrganizationReservation;
import doktoree.backend.dtos.ReservationDto;

public class ReservationMapper {

	
	
	public static ReservationDto mapToReservationDto(Reservation reservation) {
		
		ReservationDto dto = new ReservationDto();
		dto.setStartTime(reservation.getStartTime());
		dto.setEndTime(reservation.getEndTime());
		dto.setDate(reservation.getDate());
		dto.setClassrooms(reservation.getClassrooms());
		dto.setUser(reservation.getUser());
		dto.setId(reservation.getId());
		
		if(reservation instanceof ColloquiumReservation) {
			dto.setSubjectName(((ColloquiumReservation)reservation).getSubjectName());
			dto.setReservationPurpose("COLLOQUIM");
		}
		else if(reservation instanceof ExamReservation) {
			dto.setSubjectName(((ExamReservation)reservation).getSubjectName());
			dto.setReservationPurpose("EXAM");
		}
		else if(reservation instanceof CourseReservation) {
			dto.setSubjectName(((CourseReservation)reservation).getSubjectName());
			dto.setReservationPurpose("COURSE");
		}
		else if(reservation instanceof DepartmentMeetingReservation) {
			dto.setDepartment(((DepartmentMeetingReservation)reservation).getDepartment());
			dto.setReservationPurpose("DEPARTMENT");
		}
		else if(reservation instanceof CouncilReservation) {
			dto.setConucilType(((CouncilReservation) reservation).getCouncilType());
			dto.setReservationPurpose("COUNCIL");
		}
		else if(reservation instanceof OtherMeetingReservation) {
			dto.setShortDescription(((OtherMeetingReservation)reservation).getShortDescription());
			dto.setReservationPurpose("OTHER");
		}
		else if(reservation instanceof StudentOrganizationReservation) {
			dto.setStudentOrganization(((StudentOrganizationReservation)reservation).getStudentOrganization());
			dto.setReservationPurpose("STUDENT_ORGANIZATION");
		}
		else if(reservation instanceof OtherWorkshopReservation) {
			dto.setName(((OtherWorkshopReservation) reservation).getName());
			dto.setWorkshopParticipants(((OtherWorkshopReservation) reservation).getParticipants());
			dto.setReservationPurpose("OTHER_WORKSHOP");
		}
		
		
		return dto;
	}
	
}
