package doktoree.backend.services;

import doktoree.backend.dtos.DepartmentDto;
import doktoree.backend.errorresponse.Response;
import java.util.List;

public interface DepartmentService {

  public Response<List<DepartmentDto>> getAllDepartments();

}
