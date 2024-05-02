package milansomyk.testassignment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@Data
public class BodyDto<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ErrorDto> errors = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data;
    public BodyDto<T> addError(ErrorDto error) {
        this.errors.add(error);
        return this;
    }
}
