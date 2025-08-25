package doktoree.backend.mapper;

import doktoree.backend.domain.StudentOrganization;
import doktoree.backend.dtos.StudentOrganizationDto;

public class StudentOrganizationMapper {

  public static StudentOrganization mapToStudentOrganization(StudentOrganizationDto dto) {

    return new StudentOrganization(dto.getId(), dto.getName(), dto.getShortName());

  }

  public static StudentOrganizationDto mapToStudentOrganizationDto(StudentOrganization so) {

    return new StudentOrganizationDto(so.getId(), so.getName(), so.getShortName());

  }

}
