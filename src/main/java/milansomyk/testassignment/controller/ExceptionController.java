package milansomyk.testassignment.controller;

import milansomyk.testassignment.dto.ErrorDto;
import milansomyk.testassignment.dto.ErrorResponseDto;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleNotFound(Exception ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        ErrorDto errorDto = new ErrorDto();
        if(ex instanceof ChangeSetPersister.NotFoundException || ex instanceof ClassNotFoundException || ex instanceof NoResourceFoundException) {
            errorDto.fillParameters(HttpStatus.NOT_FOUND.value(),ex.getMessage());
            errorResponseDto.addErrors(errorDto);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
        }
        errorDto.fillParameters(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
        errorResponseDto.addErrors(errorDto);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
}
