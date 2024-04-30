package milansomyk.testassignment.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ErrorResponseDto {
    List<ErrorDto> errors = new ArrayList<>();
    public void addErrors(ErrorDto error){
        errors.add(error);
    }
}
