package doktoree.backend.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import doktoree.backend.domain.Classroom;
import doktoree.backend.domain.Department;
import doktoree.backend.domain.StudentOrganization;
import doktoree.backend.domain.User;
import doktoree.backend.domain.WorkshopParticipant;
import doktoree.backend.enums.CouncilType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationDto {
	
	@NotNull(message = "ID can not be null!")
	private Long id;
	
	@NotNull(message = "Reservation purpose can not be null!")
	private String reservationPurpose;
	
	@NotNull(message = "Date can not be null!")
	@FutureOrPresent(message = "Date can not be in past!")
    private LocalDate date;
	
	@NotNull(message = "Start time can not be null!")
    private LocalTime startTime;
	
	@NotNull(message = "End time can not be null!")
    private LocalTime endTime;
	
	@NotNull(message = "User can not be null!")
    private User user;
	
	@NotNull(message = "Classrooms can not be null!")
    private Set<Classroom> classrooms;
	
    private String subjectName;
    private CouncilType conucilType;
    private String shortDescription;
    private String name;
    private Department department;
    private StudentOrganization studentOrganization;
    private List<WorkshopParticipant> workshopParticipants;

}
