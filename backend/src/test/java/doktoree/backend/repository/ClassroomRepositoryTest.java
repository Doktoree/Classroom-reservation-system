package doktoree.backend.repository;

import doktoree.backend.domain.Classroom;
import doktoree.backend.enums.ClassRoomType;
import doktoree.backend.repositories.ClassroomRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ClassroomRepositoryTest {

    @Autowired
    private ClassroomRepository classroomRepository;

    private Classroom classroom1;

    private Classroom classroom2;

    @BeforeEach
    public void setup(){

        classroom1 = new Classroom();
        classroom1.setClassRoomNumber("Classroom 1");
        classroom1.setCapacity(10);
        classroom1.setClassRoomType(ClassRoomType.COMPUTER_LAB);
        classroom1.setNumberOfComputers(50);

        classroom2 = new Classroom();
        classroom2.setClassRoomNumber("Classroom 2");
        classroom2.setCapacity(11);
        classroom2.setClassRoomType(ClassRoomType.CLASSROOM);
        classroom2.setNumberOfComputers(51);

    }


    @Test
    public void saveClassroom_ReturnsClassroom(){

        classroom1.setClassRoomNumber("Classroom 1");
        classroom1.setCapacity(10);
        classroom1.setClassRoomType(ClassRoomType.COMPUTER_LAB);
        classroom1.setNumberOfComputers(50);

        Classroom savedClassroom = classroomRepository.save(classroom1);

        Assertions.assertThat(savedClassroom.getId()).isNotNull();
        Assertions.assertThat(savedClassroom).isNotNull();
        Assertions.assertThat(savedClassroom.getNumberOfComputers()).isEqualTo(classroom1.getNumberOfComputers());
        Assertions.assertThat(savedClassroom.getCapacity()).isEqualTo(classroom1.getCapacity());
        Assertions.assertThat(savedClassroom.getClassRoomType()).isEqualTo(classroom1.getClassRoomType());

    }

    @Test
    public void findAllClassrooms_ReturnsMoreThanOneClassroom(){

        classroomRepository.saveAll(List.of(classroom1,classroom2));

        List<Classroom> classrooms = classroomRepository.findAll();

        Assertions.assertThat(classrooms).isNotNull();
        Assertions.assertThat(classrooms.get(0).getCapacity()).isEqualTo(10);
        Assertions.assertThat(classrooms.get(1).getCapacity()).isEqualTo(11);
        Assertions.assertThat(classrooms.size()).isEqualTo(2);

    }

    @Test
    public void findByIdClassroom_ReturnsClassroom(){

        Classroom savedClassroom = classroomRepository.save(classroom1);

        Optional<Classroom> optionalClassroom = classroomRepository.findById(savedClassroom.getId());
        Classroom foundClassroom = optionalClassroom.get();

        Assertions.assertThat(foundClassroom).isNotNull();
        Assertions.assertThat(foundClassroom.getClassRoomNumber()).isEqualTo(foundClassroom.getClassRoomNumber());


    }

}
