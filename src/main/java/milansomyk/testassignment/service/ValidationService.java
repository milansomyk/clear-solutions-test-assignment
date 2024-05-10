package milansomyk.testassignment.service;

import lombok.extern.slf4j.Slf4j;
import milansomyk.testassignment.dto.RequestDto;
import milansomyk.testassignment.dto.UserDto;
import milansomyk.testassignment.validators.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Slf4j
@Service
public class ValidationService {
    @Autowired
    @Qualifier("UserDtoPostPutValidator")
    private CustomValidator userDtoPostPutValidator;
    @Autowired
    @Qualifier("UserDtoPatchValidator")
    private CustomValidator userDtoPatchValidator;

    public Map<String, String> validate(Object objectToValidate) {
        HashMap<String, String> errorsMap = new HashMap<>();
        if (objectToValidate instanceof RequestDto requestDto) {
            if (ObjectUtils.isEmpty(requestDto) || requestDto.getData() == null) {
                errorsMap.put("requestDto", "Exception! RequestDto is empty");
                return errorsMap;
            }
            if ((requestDto.getHttpMethod().matches("POST") || requestDto.getHttpMethod().matches("PUT")) && requestDto.getData() instanceof UserDto) {
                return userDtoPostPutValidator.validate(requestDto.getData());
            }
            if (requestDto.getHttpMethod().matches("PATCH") && requestDto.getData() instanceof UserDto) {
                return userDtoPatchValidator.validate(requestDto.getData());
            }
        }
        return errorsMap;
    }
}
