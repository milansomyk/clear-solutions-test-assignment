package milansomyk.testassignment.controller;

import milansomyk.testassignment.dto.ErrorDto;
import milansomyk.testassignment.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleNotFound(Exception ex) {
        ErrorDto error = new ErrorDto(HttpStatus.NOT_FOUND.value(),ex.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.addErrors(error);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
}
