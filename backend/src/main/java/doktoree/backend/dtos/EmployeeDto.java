package doktoree.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import doktoree.backend.enums.AcademicRank;
import doktoree.backend.enums.Title;
import doktoree.backend.validator.EnumHandler;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmployeeDto {

	@NotNull(message = "Id can't be null!")
	private Long id;

	@NotNull(message = "Name can't be null!")
	@NotEmpty(message = "Name can't be empty!")
	private String name;

	@NotNull(message = "Last name can't be null!")
	@NotEmpty(message = "Last name can't be empty!")
	private String lastName;

	@NotNull(message = "Academic rank can't be null!")
	private AcademicRank academicRank;

	@NotNull(message = "Title can't be null!")
	private Title title;

	@NotNull(message = "Department id can't be null!")
	private Long departmentId;

	@AssertTrue(message = "Academic rank must be valid value!")
	@JsonIgnore()
	public boolean isAcademicRankInScope(){

		EnumHandler<AcademicRank> enumHandler = new EnumHandler<>(AcademicRank.class);
		return enumHandler.isValid(academicRank);

	}

	@AssertTrue(message = "Title must be valid value!")
	@JsonIgnore()
	public boolean isTitleInScope(){

		EnumHandler<Title> enumHandler = new EnumHandler<>(Title.class);
		return enumHandler.isValid(title);

	}

}
