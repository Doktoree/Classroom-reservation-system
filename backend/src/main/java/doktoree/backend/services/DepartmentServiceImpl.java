package doktoree.backend.services;

import doktoree.backend.domain.Department;
import doktoree.backend.domain.Employee;
import doktoree.backend.dtos.DepartmentDto;
import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.mapper.DepartmentMapper;
import doktoree.backend.mapper.EmployeeMapper;
import doktoree.backend.repositories.DepartmentRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;

  @Autowired
  public DepartmentServiceImpl(DepartmentRepository departmentRepository) {

    this.departmentRepository = departmentRepository;

  }

  @Override
  public Response<List<DepartmentDto>> getAllDepartments() {

    List<Department> departments = departmentRepository.findAll();

    if (departments.isEmpty()) {
      throw new EmptyEntityListException("There are no departments!");
    }

    departments.forEach(e -> {

      List<EmployeeDto> employeesDto = e.getEmployees().stream()
          .map(EmployeeMapper::mapToEmployeeDto).toList();

    });

    Response<List<DepartmentDto>> response = new Response<>();
    response.setDtoT(departments.stream().map(DepartmentMapper::mapToDepartmentDto).collect(
        Collectors.toList()));
    response.setMessage("All departments successfully found!");


    return response;

  }
}
