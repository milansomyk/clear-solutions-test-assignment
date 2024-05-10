package milansomyk.testassignment.validators;

import lombok.extern.slf4j.Slf4j;
import milansomyk.testassignment.constants.Constants;
import milansomyk.testassignment.dto.UserDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component("UserDtoPostPutValidator")
@Slf4j
public class UserDtoPostPutValidator implements CustomValidator {
    @Override
    public Map<String, String> validate(Object object) {
        UserDto userDto = (UserDto) object;
        HashMap<String, String> errorsMap = new HashMap<>();
        if (!userDto.isAllRequired()) {
            log.error("Exception, not given all required User parameters!");
            errorsMap.put("Error", "Exception, not given all required User parameters!");
            return errorsMap;
        }
        if (userDto.getBirthDate().plusYears(Constants.USER_MIN_AGE).isAfter(LocalDate.now())) {
            log.error("Exception, given user birth date is less than {}", Constants.USER_MIN_AGE);
            errorsMap.put("Error", "Exception, given user birth date is less than " + Constants.USER_MIN_AGE);
            return errorsMap;
        }
        return errorsMap;
    }
}
