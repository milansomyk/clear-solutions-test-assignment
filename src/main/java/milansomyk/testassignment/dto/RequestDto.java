package milansomyk.testassignment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
public class RequestDto<T> {
    @NotNull
    @Valid
    T data;
    @JsonIgnore
    HttpMethod httpMethod;
}
