package doktoree.backend.exceptions;

import lombok.AllArgsConstructor;

public class EntityNotExistingException extends RuntimeException{

	public EntityNotExistingException(String message) {
        super(message);
    }
	
}
