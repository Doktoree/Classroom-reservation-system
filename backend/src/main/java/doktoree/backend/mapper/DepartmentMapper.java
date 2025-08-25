package doktoree.backend.mapper;

import doktoree.backend.domain.Department;
import doktoree.backend.dtos.DepartmentDto;
import java.util.stream.Collectors;

public class DepartmentMapper {


  public static Department mapToDepartment(DepartmentDto dto) {

    return new Department(dto.getId(), dto.getShortName(), dto.getName(),
        dto.getEmployees().stream().map(EmployeeMapper::mapToEmployee)
            .collect(Collectors.toList()));

  }

  public static DepartmentDto mapToDepartmentDto(Department department) {

    return new DepartmentDto(department.getId(), department.getShortName(),
        department.getName(), department.getEmployees()
        .stream().map(EmployeeMapper::mapToEmployeeDto)
        .collect(Collectors.toList()));

  }

}
