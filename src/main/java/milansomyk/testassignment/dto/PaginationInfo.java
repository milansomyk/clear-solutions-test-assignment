package milansomyk.testassignment.dto;

import lombok.Data;

@Data
public class PaginationInfo {
    Integer offset;
    Integer limit;
    Integer total;
}
