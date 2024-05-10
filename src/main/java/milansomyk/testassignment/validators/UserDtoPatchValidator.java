package milansomyk.testassignment.validators;

import lombok.extern.slf4j.Slf4j;
import milansomyk.testassignment.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("UserDtoPatchValidator")
@Slf4j
public class UserDtoPatchValidator implements CustomValidator {
    @Override
    public Map<String, String> validate(Object object) {
        UserDto userDto = (UserDto) object;
        HashMap<String, String> errorsMap = new HashMap<>();
        if (!userDto.isAnyRequired()) {
            log.error("Exception, not given any required User parameters!");
            errorsMap.put("Error", "Exception, not given any required User parameters!");
        }
        return errorsMap;
    }
}
