package doktoree.backend.factory;

import doktoree.backend.domain.*;
import doktoree.backend.dtos.ReservationDto;
import doktoree.backend.enums.ClassRoomType;
import doktoree.backend.enums.CouncilType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationFactoryTest {

    private ReservationDto dto;

    private Set<Classroom> classrooms;

    private User user;

    @BeforeEach
    public void setup(){

        Classroom classroom1 = new Classroom();
        classroom1.setId(22L);
        classroom1.setClassRoomType(ClassRoomType.COMPUTER_LAB);
        classroom1.setCapacity(10);
        classroom1.setClassRoomNumber("Classroom number");
        classroom1.setNumberOfComputers(20);

        Classroom classroom2 = new Classroom();
        classroom2.setId(23L);
        classroom2.setClassRoomType(ClassRoomType.AMPHITHEATER);
        classroom2.setCapacity(12);
        classroom2.setClassRoomNumber("Classroom number 2");
        classroom2.setNumberOfComputers(22);

        classrooms = new HashSet<>(List.of(classroom1,classroom2));

        user = new User();
        user.setId(14L);

        dto = new ReservationDto();
        dto.setId(1L);
        dto.setClassrooms(classrooms);
        dto.setUser(user);
        dto.setStartTime(LocalTime.of(17,0));
        dto.setEndTime(LocalTime.of(21,10));
        dto.setDate(LocalDate.now());
    }

    public void check(ReservationDto dto, Reservation reservation){

        Assertions.assertThat(reservation.getId()).isEqualTo(dto.getId());
        Assertions.assertThat(reservation.getUser().getId()).isEqualTo(dto.getUser().getId());
        Assertions.assertThat(reservation.getDate()).isEqualTo(dto.getDate());
        Assertions.assertThat(reservation.getStartTime()).isEqualTo(dto.getStartTime());
        Assertions.assertThat(reservation.getEndTime()).isEqualTo(dto.getEndTime());

    }

    @Test
    public void whenCreateReservation_thenReturnColloquiumReservation(){

        dto.setReservationPurpose("COLLOQUIUM");
        dto.setSubjectName("Subject name1");

        Reservation reservation = ReservationFactory.createReservation(dto);
        ColloquiumReservation colloquiumReservation = (ColloquiumReservation) reservation;
        Assertions.assertThat(colloquiumReservation.getSubjectName()).isEqualTo(dto.getSubjectName());


    }

    @Test
    public void whenCreateReservation_thenReturnExamReservation(){

        dto.setReservationPurpose("EXAM");
        dto.setSubjectName("Subject name1");

        Reservation reservation = ReservationFactory.createReservation(dto);
        ExamReservation examReservation = (ExamReservation) reservation;
        Assertions.assertThat(examReservation.getSubjectName()).isEqualTo(dto.getSubjectName());


    }

    @Test
    public void whenCreateReservation_thenReturnCourseReservation(){

        dto.setReservationPurpose("COURSE");
        dto.setSubjectName("Subject name1");

        Reservation reservation = ReservationFactory.createReservation(dto);
        CourseReservation courseReservation = (CourseReservation) reservation;
        Assertions.assertThat(courseReservation.getSubjectName()).isEqualTo(dto.getSubjectName());

    }

    @Test
    public void whenCreateReservation_thenReturnDepartmentReservation(){

        dto.setReservationPurpose("DEPARTMENT_MEETING");
        Department department = new Department();
        department.setId(2L);
        department.setName("Department");
        department.setShortName("Dep");
        dto.setDepartment(department);

        Reservation reservation = ReservationFactory.createReservation(dto);
        DepartmentMeetingReservation departmentMeetingReservation = (DepartmentMeetingReservation) reservation;
        Assertions.assertThat(departmentMeetingReservation.getDepartment()).isEqualTo(dto.getDepartment());


    }

    @Test
    public void whenCreateReservation_thenReturnCouncilReservation(){

        dto.setReservationPurpose("COUNCIL");
        dto.setCouncilType(CouncilType.DAS);

        Reservation reservation = ReservationFactory.createReservation(dto);
        CouncilReservation councilReservation = (CouncilReservation) reservation;
        Assertions.assertThat(councilReservation.getCouncilType()).isEqualTo(dto.getCouncilType());


    }

    @Test
    public void whenCreateReservation_thenReturnOtherMeetingReservation(){

        dto.setReservationPurpose("OTHER_MEETING");
        dto.setShortDescription("Short 1");

        Reservation reservation = ReservationFactory.createReservation(dto);
        OtherMeetingReservation otherMeetingReservation = (OtherMeetingReservation) reservation;
        Assertions.assertThat(otherMeetingReservation.getShortDescription()).isEqualTo(dto.getShortDescription());

    }

    @Test
    public void whenCreateReservation_thenReturnStudentOrganizationReservation(){

        dto.setReservationPurpose("STUDENT_ORGANIZATION");
        StudentOrganization studentOrganization = new StudentOrganization();
        studentOrganization.setId(44L);
        dto.setStudentOrganization(studentOrganization);

        Reservation reservation = ReservationFactory.createReservation(dto);
        StudentOrganizationReservation studentOrganizationReservation = (StudentOrganizationReservation) reservation;
        Assertions.assertThat(studentOrganizationReservation.getStudentOrganization().getId()).isEqualTo(dto.getStudentOrganization().getId());

    }


}
