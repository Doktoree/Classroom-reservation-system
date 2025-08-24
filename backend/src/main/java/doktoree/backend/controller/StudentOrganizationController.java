package doktoree.backend.controller;

import doktoree.backend.dtos.DepartmentDto;
import doktoree.backend.dtos.StudentOrganizationDto;
import doktoree.backend.errorresponse.Response;
import doktoree.backend.services.DepartmentServiceImpl;
import doktoree.backend.services.StudentOrganizationServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student-organization/")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentOrganizationController {

  private final StudentOrganizationServiceImpl studentOrganizationService;

  @Autowired
  public StudentOrganizationController(StudentOrganizationServiceImpl studentOrganizationService) {
    this.studentOrganizationService = studentOrganizationService;
  }


  @GetMapping
  public ResponseEntity<Response<List<StudentOrganizationDto>>> getAllStudentOrganizations() {

    return ResponseEntity.ok(studentOrganizationService.getAllStudentOrganizations());

  }

}
