package doktoree.backend.controller;

import doktoree.backend.dtos.DepartmentDto;
import doktoree.backend.dtos.EmployeeDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.services.DepartmentServiceImpl;
import doktoree.backend.services.EmployeeServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/department/")
@CrossOrigin(origins = "http://localhost:5173")
public class DepartmentController {


  private final DepartmentServiceImpl departmentService;

  @Autowired
  public DepartmentController(DepartmentServiceImpl departmentService) {
    this.departmentService = departmentService;
  }


  @GetMapping
  public ResponseEntity<Response<List<DepartmentDto>>> getAllDepartments() {

    return ResponseEntity.ok(departmentService.getAllDepartments());

  }
}
