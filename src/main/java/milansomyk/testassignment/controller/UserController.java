package milansomyk.testassignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import milansomyk.testassignment.dto.BodyDto;
import milansomyk.testassignment.dto.RequestDto;
import milansomyk.testassignment.dto.ResponseDto;
import milansomyk.testassignment.dto.UserDto;
import milansomyk.testassignment.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<BodyDto<UserDto>> createUser(@RequestBody @Valid RequestDto<UserDto> requestDto){
        ResponseDto<UserDto> responseDto = userService.createUser(requestDto);
        return ResponseEntity.status(responseDto.getHttpStatus()).headers(responseDto.getHttpHeaders()).body(responseDto.getBody());
    }
    @PatchMapping("/{userId}")
    public ResponseEntity<BodyDto<UserDto>> patchUser(@PathVariable UUID userId, @RequestBody @Valid RequestDto<UserDto> requestDto){
        ResponseDto<UserDto> responseDto = userService.patchUser(requestDto, userId);
        return ResponseEntity.status(responseDto.getHttpStatus()).headers(responseDto.getHttpHeaders()).body(responseDto.getBody());
    }
    @PutMapping("/{userId}")
    public ResponseEntity<BodyDto<UserDto>> updateUser(@PathVariable UUID userId, @RequestBody @Valid RequestDto<UserDto> requestDto){
        ResponseDto<UserDto> responseDto = userService.updateUser(requestDto, userId);
        return ResponseEntity.status(responseDto.getHttpStatus()).headers(responseDto.getHttpHeaders()).body(responseDto.getBody());
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<BodyDto<UserDto>> deleteUser(@PathVariable UUID userId){
        ResponseDto<UserDto> responseDto = userService.deleteUser(userId);
        return ResponseEntity.status(responseDto.getHttpStatus()).headers(responseDto.getHttpHeaders()).body(responseDto.getBody());
    }
    @GetMapping
    public ResponseEntity<BodyDto<List<UserDto>>> getUsersByBirthRange(@RequestParam String from, @RequestParam String to, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        ResponseDto<List<UserDto>> listResponseDto = userService.searchUsersByBirthDateRange(from, to, offset, limit);
        return ResponseEntity.status(listResponseDto.getHttpStatus()).headers(listResponseDto.getHttpHeaders()).body(listResponseDto.getBody());
    }
}
