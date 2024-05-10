package milansomyk.testassignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import milansomyk.testassignment.dto.*;
import milansomyk.testassignment.service.UserService;
import milansomyk.testassignment.service.ValidationService;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final ValidationService validationService;

    @PostMapping
    public ResponseEntity<BodyDto<UserDto>> createUser(@RequestBody @Valid RequestDto<UserDto> requestDto) {
        requestDto.setHttpMethod(HttpMethod.POST);
        Map<String, String> errorsMap = validationService.validate(requestDto);
        if (!errorsMap.isEmpty()) {
            BodyDto<UserDto> bodyDto =
                    new BodyDto<UserDto>().addError(new ErrorDto(HttpStatus.BAD_REQUEST.value(), errorsMap.get("Error")));
            return ResponseEntity.badRequest().body(bodyDto);
        }
        ResponseDto<UserDto> responseDto = userService.createUser(requestDto);
        return ResponseEntity.status(responseDto.getHttpStatus()).headers(responseDto.getHttpHeaders()).body(responseDto.getBody());
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<BodyDto<UserDto>> patchUser(@PathVariable UUID userId, @RequestBody @Valid RequestDto<UserDto> requestDto) {
        requestDto.setHttpMethod(HttpMethod.PATCH);
        Map<String, String> errorsMap = validationService.validate(requestDto);
        if (!errorsMap.isEmpty()) {
            BodyDto<UserDto> bodyDto =
                    new BodyDto<UserDto>().addError(new ErrorDto(HttpStatus.BAD_REQUEST.value(), errorsMap.get("Error")));
            return ResponseEntity.badRequest().body(bodyDto);
        }
        ResponseDto<UserDto> responseDto = userService.patchUser(requestDto, userId);
        return ResponseEntity.status(responseDto.getHttpStatus()).headers(responseDto.getHttpHeaders()).body(responseDto.getBody());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<BodyDto<UserDto>> updateUser(@PathVariable UUID userId, @RequestBody @Valid RequestDto<UserDto> requestDto) {
        requestDto.setHttpMethod(HttpMethod.PUT);
        Map<String, String> errorsMap = validationService.validate(requestDto);
        if (!errorsMap.isEmpty()) {
            BodyDto<UserDto> bodyDto =
                    new BodyDto<UserDto>().addError(new ErrorDto(HttpStatus.BAD_REQUEST.value(), errorsMap.get("Error")));
            return ResponseEntity.badRequest().body(bodyDto);
        }
        ResponseDto<UserDto> responseDto = userService.updateUser(requestDto, userId);
        return ResponseEntity.status(responseDto.getHttpStatus()).headers(responseDto.getHttpHeaders()).body(responseDto.getBody());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<BodyDto<UserDto>> deleteUser(@PathVariable UUID userId) {
        ResponseDto<UserDto> responseDto = userService.deleteUser(userId);
        return ResponseEntity.status(responseDto.getHttpStatus()).headers(responseDto.getHttpHeaders()).body(responseDto.getBody());
    }

    @GetMapping
    public ResponseEntity<BodyDto<List<UserDto>>> getUsersByBirthRange(@RequestParam String from, @RequestParam String to, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, defaultValue = "100") Integer limit) {
        ResponseDto<List<UserDto>> listResponseDto = userService.searchUsersByBirthDateRange(from, to, offset, limit);
        return ResponseEntity.status(listResponseDto.getHttpStatus()).headers(listResponseDto.getHttpHeaders()).body(listResponseDto.getBody());
    }
}
