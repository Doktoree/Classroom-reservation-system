package doktoree.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import doktoree.backend.domain.Department;
import doktoree.backend.dtos.DepartmentDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.repositories.DepartmentRepository;
import doktoree.backend.services.DepartmentServiceImpl;
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
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department1;

    @BeforeEach
    public void setup(){

      department1 = new Department();
      department1.setName("Department 1");
      department1.setShortName("Dep1");
      department1.setId(2L);
      department1.setEmployees(new ArrayList<>());

    }

  public void check(DepartmentDto dto, Department d){
    assertThat(dto).isNotNull();
    assertThat(dto.getName()).isEqualTo(d.getName());
    assertThat(dto.getShortName()).isEqualTo(d.getShortName());
    assertThat(dto.getId()).isEqualTo(d.getId());
  }

  @DisplayName("Get all departments - should return list of DTOs")
  @Test
  public void whenGetAllDepartments_thenReturnsExpectedResponse(){


    Department department2 = new Department();
    department2.setId(3L);
    department2.setName("Department 2");
    department2.setShortName("Dep2");
    department2.setEmployees(new ArrayList<>());

    List<Department> departmentList = List.of(department1,department2);

    Mockito.when(departmentRepository.findAll()).thenReturn(departmentList);

    Response<List<DepartmentDto>> response = departmentService.getAllDepartments();
    assertThat(response.getDtoT()).isNotEmpty();
    assertThat(response.getDtoT()).isNotNull();
    check(response.getDtoT().get(0), department1);
    check(response.getDtoT().get(1), department2);
    assertThat(response.getDtoT().size()).isEqualTo(2);

  }

  @DisplayName("Get all departments - should throw EmptyEntityListException if list is empty")
  @Test
  public void whenGetAllDepartments_thenThrowException(){

    Mockito.when(departmentRepository.findAll()).thenReturn(new ArrayList<>());

    assertThatThrownBy(()->{

      departmentService.getAllDepartments();

    }).isInstanceOf(EmptyEntityListException.class)
        .hasMessageContaining("There are no departments!");




  }



}
