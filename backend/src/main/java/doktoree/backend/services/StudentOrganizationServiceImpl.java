package doktoree.backend.services;

import doktoree.backend.domain.Department;
import doktoree.backend.domain.StudentOrganization;
import doktoree.backend.dtos.DepartmentDto;
import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.dtos.StudentOrganizationDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.exceptions.EmptyEntityListException;
import doktoree.backend.mapper.DepartmentMapper;
import doktoree.backend.mapper.EmployeeMapper;
import doktoree.backend.mapper.StudentOrganizationMapper;
import doktoree.backend.repositories.StudentOrganizationRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentOrganizationServiceImpl implements StudentOrganizationService{

  public final StudentOrganizationRepository repository;

  @Autowired
  public StudentOrganizationServiceImpl(StudentOrganizationRepository repository){

    this.repository = repository;

  }

  @Override
  public Response<List<StudentOrganizationDto>> getAllStudentOrganizations() {
    List<StudentOrganization> studentOrganizations = repository.findAll();

    if (studentOrganizations.isEmpty()) {
      throw new EmptyEntityListException("There are no student organizations!");
    }

    Response<List<StudentOrganizationDto>> response = new Response<>();
    response.setDtoT(studentOrganizations.stream().map(StudentOrganizationMapper::mapToStudentOrganizationDto).collect(
        Collectors.toList()));
    response.setMessage("All student organizations successfully found!");


    return response;
  }
}
