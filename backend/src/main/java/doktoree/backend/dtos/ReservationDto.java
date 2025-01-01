package doktoree.backend.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import doktoree.backend.domain.Classroom;
import doktoree.backend.domain.Department;
import doktoree.backend.domain.StudentOrganization;
import doktoree.backend.domain.User;
import doktoree.backend.enums.CouncilType;
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
	
	private Long id;
	private String reservationPurpose;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private User user;
    private Classroom classroom;
    private String subjectName;
    private CouncilType conucilType;
    private String shortDescription;
    private String name;
    private Department department;
    private StudentOrganization studentOrganization;

}
