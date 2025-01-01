package doktoree.backend.exceptions;

public class InvalidForeignKeyException extends RuntimeException {

	public InvalidForeignKeyException(String message) {
		
		super(message);
		
	}
	
}
