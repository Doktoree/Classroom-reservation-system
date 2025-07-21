package doktoree.backend.repository;

import doktoree.backend.domain.*;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.ClassRoomType;
import doktoree.backend.enums.Role;
import doktoree.backend.enums.Title;
import doktoree.backend.repositories.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Reservation reservation;

    private User user;

    private Set<Classroom> classrooms;

    private Reservation reservation2;


    @BeforeEach
    public void setup(){


        Classroom classroom1 = new Classroom();
        classroom1.setCapacity(20);
        classroom1.setClassRoomType(ClassRoomType.COMPUTER_LAB);
        classroom1.setClassRoomNumber("Classroom 1");
        classroom1.setNumberOfComputers(40);
        Classroom savedClassroom1 = classroomRepository.save(classroom1);

        Classroom classroom2 = new Classroom();
        classroom2.setCapacity(15);
        classroom2.setClassRoomType(ClassRoomType.COMPUTER_LAB);
        classroom2.setClassRoomNumber("Classroom 2");
        classroom2.setNumberOfComputers(30);
        Classroom savedClassroom2 = classroomRepository.save(classroom2);

        classrooms = new HashSet<>(List.of(savedClassroom1,savedClassroom2));

        Department department = new Department();
        department.setName("Department");
        department.setShortName("dep");
        Department savedDepartment = departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setTitle(Title.MD);
        employee.setAcademicRank(AcademicRank.FULL_PROFESSOR);
        employee.setLastName("Last");
        employee.setName("Name");
        employee.setDepartment(savedDepartment);
        Employee savedEmployee = employeeRepository.save(employee);


        user = new User();
        user.setRole(Role.USER);
        user.setPassword("pass");
        user.setEmail("mail@gmail.com");
        user.setEmployee(savedEmployee);
        user = userRepository.save(user);

        reservation = new Reservation();
        reservation.setDate(LocalDate.now());
        reservation.setStartTime(LocalTime.of(12,15));
        reservation.setEndTime(LocalTime.of(14,15));
        reservation.setUser(user);

        reservation2 = new Reservation();
        reservation2.setDate(LocalDate.now());
        reservation2.setStartTime(LocalTime.of(15,15));
        reservation2.setEndTime(LocalTime.of(16,15));
        reservation2.setUser(user);
    }

    @Test
    public void whenFindByUser_thenReturnPage(){


        reservationRepository.saveAll(List.of(reservation,reservation2));

        Page<Reservation> page = reservationRepository.findByUser(user, PageRequest.of(0,10));
        List<Reservation> reservations = page.getContent();
        Assertions.assertThat(reservations.size()).isEqualTo(2);
        Assertions.assertThat(reservations.get(0).getId()).isEqualTo(reservation.getId());
        Assertions.assertThat(reservations.get(1).getId()).isEqualTo(reservation2.getId());
    }

    @Test
    public void whenFindAll_thenReturnPage(){

        reservationRepository.saveAll(List.of(reservation,reservation2));
        Page<Reservation> page = reservationRepository.findAll(PageRequest.of(0,10));
        List<Reservation> reservations = page.getContent();
        Assertions.assertThat(reservations.size()).isEqualTo(2);
        Assertions.assertThat(reservations.get(0).getId()).isEqualTo(reservation.getId());
        Assertions.assertThat(reservations.get(1).getId()).isEqualTo(reservation2.getId());

    }

    @Test
    public void whenFindByUser_thenReturnList(){

        reservationRepository.saveAll(List.of(reservation,reservation2));
        List<Reservation> reservations = reservationRepository.findByUser(user);
        Assertions.assertThat(reservations.size()).isEqualTo(2);
        Assertions.assertThat(reservations.get(0).getId()).isEqualTo(reservation.getId());
        Assertions.assertThat(reservations.get(1).getId()).isEqualTo(reservation2.getId());
    }

    @Test
    public void whenFindByDateAndStartTimeGreaterThanAndEndTimeLessThan_thenReturnList(){

        reservationRepository.saveAll(List.of(reservation,reservation2));
        List<Reservation> reservations = reservationRepository
                .findByDateAndStartTimeAfterAndEndTimeBefore(LocalDate.now(), LocalTime.of(12,1), LocalTime.of(17,0));
        Assertions.assertThat(reservations.size()).isEqualTo(2);
        Assertions.assertThat(reservations.get(0).getId()).isEqualTo(reservation.getId());
        Assertions.assertThat(reservations.get(1).getId()).isEqualTo(reservation2.getId());
    }
}
