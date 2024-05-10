package milansomyk.testassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    int status;
    String detail;

    public void fillParameters(int statusValue, String detailMessage) {
        this.status = statusValue;
        this.detail = detailMessage;
    }
}
