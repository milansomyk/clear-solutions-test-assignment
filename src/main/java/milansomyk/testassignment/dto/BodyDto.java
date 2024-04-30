package milansomyk.testassignment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
@Data
@AllArgsConstructor
public class BodyDto<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ErrorDto> errors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data;
}
