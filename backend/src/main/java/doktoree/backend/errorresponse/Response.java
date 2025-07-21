package doktoree.backend.errorresponse;

import java.time.LocalTime;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response<DtoT> {

	private DtoT dtoT;
	private String message;
	private LocalTime time = LocalTime.now();
}
