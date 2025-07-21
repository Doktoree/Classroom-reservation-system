package doktoree.backend.service;

import doktoree.backend.domain.Classroom;
import doktoree.backend.dtos.ClassroomDto;
import doktoree.backend.enums.ClassRoomType;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.exceptions.EntityNotDeletedException;
import doktoree.backend.exceptions.EntityNotExistingException;
import doktoree.backend.exceptions.EntityNotSavedException;
import doktoree.backend.repositories.ClassroomRepository;
import doktoree.backend.services.ClassroomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;

    @InjectMocks
    private ClassroomServiceImpl classroomService;

    private Classroom classroom;

    @BeforeEach
    public void setup(){

        classroom = new Classroom();
        classroom.setNumberOfComputers(40);
        classroom.setCapacity(20);
        classroom.setClassRoomType(ClassRoomType.COMPUTER_LAB);
        classroom.setClassRoomNumber("Classroom 1");

    }

    @DisplayName("Find classroom by valid ID - should return expected DTO")
    @Test
    public void validId_whenFindClassroomById_thenReturnsExpectedDto(){

        classroom.setId(1L);
        when(classroomRepository.findById(1L)).thenReturn(Optional.ofNullable(classroom));
        Response<ClassroomDto> response = classroomService.findClassroomById(1L);
        ClassroomDto dto = response.getDtoT();
        assertThat(dto).isNotNull();
        assertThat(dto.getCapacity()).isEqualTo(20);
        assertThat(dto.getClassRoomType()).isEqualTo(ClassRoomType.COMPUTER_LAB);
        assertThat(dto.getClassRoomNumber()).isEqualTo("Classroom 1");
        assertThat(dto.getNumberOfComputers()).isEqualTo(40);
        assertThat(dto.getId()).isEqualTo(classroom.getId()).isEqualTo(1L);
        assertThat(response.getMessage()).isEqualTo("Classroom found successfully!");
    }

    @DisplayName("Find classroom by invalid ID - should throw EntityNotExistingException")
    @Test
    public void invalidId_whenFindClassroomById_thenThrowException(){

        classroom.setId(1L);
        when(classroomRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {

            classroomService.findClassroomById(99L);

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not classroom with given ID!");


    }

    @DisplayName("Save classroom - should return expected DTO")
    @Test
    public void whenSaveClassroom_thenReturnsExpectedDto(){

        when(classroomRepository.save(Mockito.any(Classroom.class))).thenReturn(classroom);
        ClassroomDto dto = ClassroomDto.builder()
                .classRoomNumber("Classroom 1")
                .numberOfComputers(40)
                .capacity(20)
                .classRoomType(ClassRoomType.COMPUTER_LAB)
                .build();
        Response<ClassroomDto> response = classroomService.saveClassroom(dto);
        ClassroomDto responseDto = response.getDtoT();
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getCapacity()).isEqualTo(classroom.getCapacity());
        assertThat(responseDto.getNumberOfComputers()).isEqualTo(classroom.getNumberOfComputers());
        assertThat(responseDto.getClassRoomNumber()).isEqualTo(classroom.getClassRoomNumber());
        assertThat(responseDto.getClassRoomType()).isEqualTo(classroom.getClassRoomType());
        assertThat(response.getMessage()).isEqualTo("Classroom successfully saved!");
        verify(classroomRepository).save(any(Classroom.class));

    }

    @DisplayName("Save classroom - should throw EntityNotSavedException on failure")
    @Test
    public void whenSaveClassroom_thenThrowsException(){


            when(classroomRepository.save(any(Classroom.class))).thenThrow(new EntityNotSavedException("Classroom can not be saved!"));
            ClassroomDto dto = ClassroomDto.builder()
                .classRoomNumber("Classroom 1")
                .numberOfComputers(40)
                .capacity(20)
                .classRoomType(ClassRoomType.COMPUTER_LAB)
                .build();

            assertThatThrownBy(() -> {

                classroomService.saveClassroom(dto);

            }).isInstanceOf(EntityNotSavedException.class)
                    .hasMessageContaining("Classroom can not be saved!");

    }

    @DisplayName("Delete classroom with invalid ID - should throw EntityNotExistingException")
    @Test
    public void invalidId_whenDeletesClassroom_thenReturnsExpectedDto(){

        classroom.setId(1L);
        when(classroomRepository.findById(classroom.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {

            classroomService.deleteClassroom(classroom.getId());

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not classroom with given ID!");

    }

    @DisplayName("Delete classroom - should throw EntityNotDeletedException on failure")
    @Test
    public void validId_whenSavesClassroom_thenThrowsException(){

        classroom.setId(1L);
        when(classroomRepository.findById(classroom.getId())).thenReturn(Optional.ofNullable(classroom));
        doThrow(new EntityNotDeletedException("Classroom can not be deleted"))
                .when(classroomRepository)
                .deleteById(classroom.getId());

        assertThatThrownBy(() -> {

            classroomService.deleteClassroom(classroom.getId());
        }).isInstanceOf(EntityNotDeletedException.class)
                .hasMessageContaining("Classroom can not be deleted");


    }

    @DisplayName("Delete classroom - should return successful response")
    @Test
    public void validId_whenSavesClassroom_thenReturnsExpectedResponse(){

        classroom.setId(1L);
        when(classroomRepository.findById(classroom.getId())).thenReturn(Optional.ofNullable(classroom));
        Response<ClassroomDto> response = classroomService.deleteClassroom(classroom.getId());
        assertThat(response.getMessage()).isEqualTo("Classroom successfully deleted!");
        assertThat(response.getDtoT().getId()).isEqualTo(classroom.getId());
        assertThat(response.getDtoT().getCapacity()).isEqualTo(classroom.getCapacity());
        verify(classroomRepository, times(1)).deleteById(classroom.getId());

    }

    @DisplayName("Get all classrooms - should return list of DTOs")
    @Test
    public void whenGetsAllClassrooms_thenReturnsExpectedResponse(){

        Classroom classroom2 = new Classroom();
        classroom2.setId(2L);
        classroom2.setCapacity(49);
        classroom2.setClassRoomType(ClassRoomType.AMPHITHEATER);
        classroom2.setNumberOfComputers(9);

        List<Classroom> classrooms = List.of(classroom,classroom2);

        when(classroomRepository.findAll()).thenReturn(classrooms);
        Response<List<ClassroomDto>> response = classroomService.getAllClassrooms();

        assertThat(response.getDtoT()).isNotNull();
        assertThat(response.getDtoT().size()).isEqualTo(2);
        assertThat(response.getDtoT().get(0).getId()).isEqualTo(classroom.getId());
        assertThat(response.getDtoT().get(1).getId()).isEqualTo(classroom2.getId());
        assertThat(response.getMessage()).isEqualTo("All classrooms found successfully!");
        verify(classroomRepository, times(1)).findAll();



    }

    @DisplayName("Get all classrooms - should throw EmptyEntityListException if list is empty")
    @Test
    public void whenGetsAllClassrooms_thenThrowsException(){

        doThrow(new EmptyEntityListException("There are no classrooms!")).when(classroomRepository).findAll();
        assertThatThrownBy(() -> {

            classroomService.getAllClassrooms();

        }).isInstanceOf(EmptyEntityListException.class)
                .hasMessageContaining("There are no classrooms!");


    }

    @DisplayName("Update classroom with invalid ID - should throw EntityNotExistingException")
    @Test
    public void invalidId_whenUpdatesClassroom_thenThrowsException(){

        ClassroomDto classroomDto = new ClassroomDto();
        classroomDto.setId(2L);
        classroomDto.setCapacity(9);
        classroomDto.setClassRoomNumber("Classroom 5");
        classroomDto.setNumberOfComputers(10);

        doThrow(new EntityNotExistingException("There is not classroom with given ID!"))
                .when(classroomRepository).findById(2L);

        assertThatThrownBy(() -> {

            classroomService.updateClassroom(classroomDto);

        }).isInstanceOf(EntityNotExistingException.class)
                .hasMessageContaining("There is not classroom with given ID!");

        verify(classroomRepository, times(1)).findById(2L);

    }

    @DisplayName("Update classroom - should throw EntityNotSavedException on failure")
    @Test
    public void validId_whenUpdatesClassroom_thenThrowsException(){

        ClassroomDto classroomDto = new ClassroomDto();
        classroomDto.setId(1L);
        classroomDto.setCapacity(40);
        classroomDto.setClassRoomNumber("Classroom 1");
        classroomDto.setNumberOfComputers(20);
        classroomDto.setClassRoomType(ClassRoomType.COMPUTER_LAB);

        when(classroomRepository.findById(classroomDto.getId())).thenReturn(Optional.ofNullable(classroom));
        doThrow(new EntityNotSavedException("Classroom can not be updated!"))
                .when(classroomRepository)
                .save(classroom);

        assertThatThrownBy(() -> {

            classroomService.updateClassroom(classroomDto);

        }).isInstanceOf(EntityNotSavedException.class)
                .hasMessageContaining("Classroom can not be updated!");

        verify(classroomRepository, times(1)).findById(1L);
        verify(classroomRepository, times(1)).save(classroom);

    }

    @DisplayName("Update classroom - should return updated DTO")
    @Test
    public void validId_whenUpdatesClassroom_thenReturnsExceptedValue(){

        classroom.setId(1L);

        ClassroomDto classroomDto = new ClassroomDto();
        classroomDto.setId(1L);
        classroomDto.setCapacity(40);
        classroomDto.setClassRoomNumber("Classroom 1");
        classroomDto.setNumberOfComputers(20);
        classroomDto.setClassRoomType(ClassRoomType.COMPUTER_LAB);

        when(classroomRepository.findById(classroomDto.getId())).thenReturn(Optional.ofNullable(classroom));
        when(classroomRepository.save(classroom)).thenReturn(classroom);

        Response<ClassroomDto> response = classroomService.updateClassroom(classroomDto);

        assertThat(response.getDtoT()).isNotNull();
        assertThat(response.getDtoT().getId()).isEqualTo(classroomDto.getId());
        assertThat(response.getMessage()).isEqualTo("Classroom successfully updated!");

        verify(classroomRepository, times(1)).findById(1L);
        verify(classroomRepository, times(1)).save(classroom);

    }

}
