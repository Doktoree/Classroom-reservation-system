package doktoree.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import doktoree.backend.domain.Employee;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DepartmentDto{

  private Long id;

  private String shortName;

  private String name;

  private List<EmployeeDto> employees;


}
