package doktoree.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import doktoree.backend.error_response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotExistingException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotExistingException(EntityNotExistingException ex){
		
		ErrorResponse errorResponse = new ErrorResponse("Entity not existing", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		
	}
	
	@ExceptionHandler(EntityNotSavedException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotSavedException(EntityNotSavedException ex){
		
		ErrorResponse errorResponse = new ErrorResponse("Entity not saved", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		
	}
	
	@ExceptionHandler(EntityNotDeletedException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotDeletedException(EntityNotDeletedException ex){
		
		ErrorResponse errorResponse = new ErrorResponse("Entity not deleted", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		
	}
	
	@ExceptionHandler(EmptyEntityListException.class)
	public ResponseEntity<ErrorResponse> handleEmptyEntityListException(EmptyEntityListException ex){
		
		ErrorResponse errorResponse = new ErrorResponse("List of entities is empty!", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		
	}
	
	@ExceptionHandler(InvalidForeignKeyException.class)
	public ResponseEntity<ErrorResponse> handleInvalidForeignKeyException(InvalidForeignKeyException ex){
		
		ErrorResponse errorResponse = new ErrorResponse("Foreign key is not valid!", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex){
		
		ErrorResponse errorResponse = new ErrorResponse("Illegal argument exception!", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		
	}
	
}
