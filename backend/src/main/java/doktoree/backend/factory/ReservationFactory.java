package doktoree.backend.factory;

import org.springframework.stereotype.Component;

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

@Component
public class ReservationFactory {

	public static Reservation createReservation(ReservationDto dto) {
		
		Reservation reservation;
		
		
		switch (dto.getReservationPurpose()) {
		
		case "COLLOQUIM":
			reservation = new ColloquiumReservation();
			((ColloquiumReservation) reservation).setSubjectName(dto.getSubjectName());
			break;

		case "EXAM":
			reservation = new ExamReservation();
			((ExamReservation) reservation).setSubjectName(dto.getSubjectName());
			break;
			
		case "COURSE":
			reservation = new CourseReservation();
			((CourseReservation) reservation).setSubjectName(dto.getSubjectName());
			break;
			
		case "DEPARTMENT":
			reservation = new DepartmentMeetingReservation();
			((DepartmentMeetingReservation) reservation).setDepartment(dto.getDepartment());
			break;
			
		case "COUNCIL":
			reservation = new CouncilReservation();
			((CouncilReservation) reservation).setCouncilType(dto.getConucilType());
			break;
			
		case "OTHER":
			reservation = new OtherMeetingReservation();
			((OtherMeetingReservation) reservation).setShortDescription(dto.getShortDescription());
			break;
			
		case "STUDENT_ORGANIZATION":
			reservation = new StudentOrganizationReservation();
			((StudentOrganizationReservation) reservation).setStudentOrganization(dto.getStudentOrganization());
			break;
			
		case "OTHER_WORKSHOP":
			reservation = new OtherWorkshopReservation();
			((OtherWorkshopReservation) reservation).setName(dto.getName());
			break;
			
		default:
			reservation = new Reservation();
		}
		
		reservation.setDate(dto.getDate());
		reservation.setPurpose(dto.getReservationPurpose());
		reservation.setEndTime(dto.getEndTime());
		reservation.setStartTime(dto.getStartTime());
		reservation.setClassroom(dto.getClassroom());
		reservation.setUser(dto.getUser());
		reservation.setId(dto.getId());
		
		return reservation;
	}
	
	
}
