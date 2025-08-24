package doktoree.backend.services;

import doktoree.backend.dtos.DepartmentDto;
import doktoree.backend.dtos.StudentOrganizationDto;
import doktoree.backend.errorresponse.Response;
import java.util.List;

public interface StudentOrganizationService {

  public Response<List<StudentOrganizationDto>> getAllStudentOrganizations();

}
