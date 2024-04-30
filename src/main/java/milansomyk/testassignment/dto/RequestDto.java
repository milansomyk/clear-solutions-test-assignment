package milansomyk.testassignment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestDto<T> {
    @NotNull
    @Valid
    T data;
}
