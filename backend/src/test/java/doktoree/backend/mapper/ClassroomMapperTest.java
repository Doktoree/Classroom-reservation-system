package doktoree.backend.mapper;

import doktoree.backend.domain.Classroom;
import doktoree.backend.dtos.ClassroomDto;
import doktoree.backend.enums.ClassRoomType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassroomMapperTest {

    @Test
    public void whenMapToClassroomDto(){

        Classroom classroom = new Classroom();
        classroom.setId(1L);
        classroom.setNumberOfComputers(20);
        classroom.setClassRoomNumber("Classroom number");
        classroom.setCapacity(40);
        classroom.setClassRoomType(ClassRoomType.COMPUTER_LAB);

        ClassroomDto dto = ClassroomMapper.mapToClassroomDto(classroom);
        Assertions.assertThat(dto.getId()).isEqualTo(classroom.getId());
        Assertions.assertThat(dto.getCapacity()).isEqualTo(classroom.getCapacity());
        Assertions.assertThat(dto.getClassRoomNumber()).isEqualTo(classroom.getClassRoomNumber());
        Assertions.assertThat(dto.getNumberOfComputers()).isEqualTo(classroom.getNumberOfComputers());
        Assertions.assertThat(dto.getClassRoomType()).isEqualTo(classroom.getClassRoomType());
    }

    @Test
    public void whenMapToClassroom(){

        ClassroomDto dto = new ClassroomDto();
        dto.setId(1L);
        dto.setNumberOfComputers(20);
        dto.setClassRoomNumber("Classroom number");
        dto.setCapacity(40);
        dto.setClassRoomType(ClassRoomType.COMPUTER_LAB);

        Classroom classroom = ClassroomMapper.mapToClassroom(dto);

        Assertions.assertThat(dto.getId()).isEqualTo(classroom.getId());
        Assertions.assertThat(dto.getCapacity()).isEqualTo(classroom.getCapacity());
        Assertions.assertThat(dto.getClassRoomNumber()).isEqualTo(classroom.getClassRoomNumber());
        Assertions.assertThat(dto.getNumberOfComputers()).isEqualTo(classroom.getNumberOfComputers());
        Assertions.assertThat(dto.getClassRoomType()).isEqualTo(classroom.getClassRoomType());

    }

}
