package doktoree.backend.error_response;

import java.time.LocalTime;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response<DTO> {

	private DTO dto;
	private String message;
	private LocalTime time = LocalTime.now();
}
