package milansomyk.testassignment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseDto<D> {
    @JsonIgnore
    HttpStatus httpStatus;
    @JsonIgnore
    HttpHeaders httpHeaders;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    BodyDto<D> body;
    public ResponseDto<D> fillParameters(HttpStatus httpStatus, HttpHeaders httpHeaders, D data, ErrorDto error){
        this.httpStatus = httpStatus;
        this.httpHeaders = httpHeaders;
        BodyDto<D> bodyDto = new BodyDto<>();
        bodyDto.setData(data);
        if(error != null){
            List<ErrorDto> errors = new ArrayList<>();
            errors.add(error);
            bodyDto.errors = errors;
        }else{
            bodyDto.errors = null;
        }
        this.body = bodyDto;
        return this;
    }
    public ResponseDto<D> setErrorResponse(HttpStatus httpStatus, ErrorDto error){
        BodyDto<D> bodyDto = new BodyDto<>();
        this.httpHeaders = HttpHeaders.EMPTY;
        this.httpStatus = httpStatus;
        this.body = bodyDto.addError(error);
        return this;
    }
}
