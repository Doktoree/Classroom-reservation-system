package doktoree.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import doktoree.backend.domain.StudentOrganization;
import doktoree.backend.dtos.StudentOrganizationDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.repositories.StudentOrganizationRepository;
import doktoree.backend.services.StudentOrganizationServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudentOrganizationServiceTest {

    @Mock
    private StudentOrganizationRepository studentOrganizationRepository;

    @InjectMocks
    private StudentOrganizationServiceImpl studentOrganizationService;

    private StudentOrganization studentOrganization;

  @BeforeEach
  public void setup(){

    studentOrganization = new StudentOrganization();
    studentOrganization.setName("Student organization 1");
    studentOrganization.setShortName("Std1");
    studentOrganization.setId(2L);

  }

  public void check(StudentOrganizationDto dto, StudentOrganization s){
    assertThat(dto).isNotNull();
    assertThat(dto.getName()).isEqualTo(s.getName());
    assertThat(dto.getShortName()).isEqualTo(s.getShortName());
    assertThat(dto.getId()).isEqualTo(s.getId());
  }

  @DisplayName("Get all student organizations - should return list of DTOs")
  @Test
  public void whenGetAllStudentOrganizations_thenReturnsExpectedResponse(){


    StudentOrganization so = new StudentOrganization();
    so.setName("Student organization 2");
    so.setShortName("Std2");
    so.setId(3L);


    List<StudentOrganization> studentOrganizations = List.of(studentOrganization,so);

    Mockito.when(studentOrganizationRepository.findAll()).thenReturn(studentOrganizations);

    Response<List<StudentOrganizationDto>> response = studentOrganizationService.getAllStudentOrganizations();
    assertThat(response.getDtoT()).isNotEmpty();
    assertThat(response.getDtoT()).isNotNull();
    check(response.getDtoT().get(0), studentOrganization);
    check(response.getDtoT().get(1), so);
    assertThat(response.getDtoT().size()).isEqualTo(2);

  }

  @DisplayName("Get all student organizations - should throw EmptyEntityListException if list is empty")
  @Test
  public void whenGetAllDepartments_thenThrowException(){

    Mockito.when(studentOrganizationRepository.findAll()).thenReturn(new ArrayList<>());

    assertThatThrownBy(()->{

      studentOrganizationService.getAllStudentOrganizations();

    }).isInstanceOf(EmptyEntityListException.class)
        .hasMessageContaining("There are no student organizations!");




  }


}
