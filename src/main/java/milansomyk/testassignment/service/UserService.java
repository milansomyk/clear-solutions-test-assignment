package milansomyk.testassignment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import milansomyk.testassignment.dto.ErrorDto;
import milansomyk.testassignment.dto.RequestDto;
import milansomyk.testassignment.dto.ResponseDto;
import milansomyk.testassignment.dto.UserDto;
import milansomyk.testassignment.entity.User;
import milansomyk.testassignment.mapper.UserMapper;
import milansomyk.testassignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    @Value("${user.minAge}")
    private Integer userMinAge;

    public ResponseDto<UserDto> createUser(RequestDto<UserDto> requestDto) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        ErrorDto errorDto = new ErrorDto();
        if (requestDto == null || requestDto.getData() == null) {
            return errorHandler(responseDto, errorDto, "Exception, given User data is null!", HttpStatus.BAD_REQUEST);
        }
        if (!requestDto.getData().isAllRequired()) {
            return errorHandler(responseDto, errorDto, "Exception, not given all required User parameters!", HttpStatus.BAD_REQUEST);
        }
        UserDto requestedUser = requestDto.getData();
        if (requestedUser.getBirthDate().plusYears(userMinAge).isAfter(LocalDate.now())) {
            return errorHandler(responseDto, errorDto, "Exception, given user birth date is less than " + userMinAge + " years!", HttpStatus.BAD_REQUEST);
        }
        User user = userMapper.fromDto(requestedUser);
        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            return errorHandler(responseDto, errorDto, "Exception while trying to save a user! Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/users/" + savedUser.getId()));
        return responseDto.fillParameters(HttpStatus.CREATED, httpHeaders, null, null);
    }
    public ResponseDto<UserDto> patchUser(RequestDto<UserDto> requestDto, Integer userId) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        ErrorDto errorDto = new ErrorDto();
        if (requestDto == null || requestDto.getData() == null) {
            return errorHandler(responseDto, errorDto, "Exception, given user data!", HttpStatus.BAD_REQUEST);
        }
        if (!requestDto.getData().isAnyRequired()) {
            return errorHandler(responseDto, errorDto, "Exception, not given any required User parameters!", HttpStatus.BAD_REQUEST);
        }
        if (userId == null) {
            return errorHandler(responseDto, errorDto, "Exception, given user id is null!", HttpStatus.BAD_REQUEST);
        }
        UserDto requestedUser = requestDto.getData();
        User savedUser;
        try {
            savedUser = userRepository.findById(userId).orElse(null);
        } catch (Exception e) {
            return errorHandler(responseDto, errorDto, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (savedUser == null) {
            return errorHandler(responseDto, errorDto, "Exception! User with this id: " + userId + " was not found!", HttpStatus.BAD_REQUEST);
        }
        User changedUser = updateExistedFields(requestedUser, savedUser);
        try {
            userRepository.save(changedUser);
        } catch (Exception e) {
            return errorHandler(responseDto, errorDto, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseDto.fillParameters(HttpStatus.OK, HttpHeaders.EMPTY, null, null);
    }
    public ResponseDto<UserDto> updateUser(RequestDto<UserDto> requestDto, Integer userId) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        ErrorDto errorDto = new ErrorDto();
        if (requestDto == null || requestDto.getData() == null) {
            return errorHandler(responseDto, errorDto, "Exception, given user data is null!", HttpStatus.BAD_REQUEST);
        }
        if (!requestDto.getData().isAllFieldsNotNull()) {
            return errorHandler(responseDto, errorDto, "Exception, not given all User parameters!", HttpStatus.BAD_REQUEST);
        }
        if (userId == null) {
            return errorHandler(responseDto, errorDto, "Exception, given user id is null!", HttpStatus.BAD_REQUEST);
        }
        UserDto requestedUser = requestDto.getData();
        User savedUser;
        try {
            savedUser = userRepository.findById(userId).orElse(null);
        } catch (Exception e) {
            return errorHandler(responseDto, errorDto, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (savedUser == null) {
            return errorHandler(responseDto, errorDto, "Exception! User with this id: " + userId + " was not found!", HttpStatus.BAD_REQUEST);
        }
        updateExistedFields(requestedUser, savedUser);
        try {
            userRepository.save(savedUser);
        } catch (Exception e) {
            return errorHandler(responseDto, errorDto, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseDto.fillParameters(HttpStatus.OK, HttpHeaders.EMPTY, null, null);
    }
    public ResponseDto<UserDto> deleteUser(Integer userId) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        ErrorDto errorDto = new ErrorDto();
        if (userId == null) {
            return errorHandler(responseDto, errorDto, "Exception, given user id is null!", HttpStatus.BAD_REQUEST);
        }
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            return errorHandler(responseDto, errorDto, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseDto.fillParameters(HttpStatus.OK, HttpHeaders.EMPTY, null, null);
    }
    public ResponseDto<List<UserDto>> searchUsersByBirthDateRange(String from, String to) {
        ResponseDto<List<UserDto>> responseDto = new ResponseDto<>();
        ErrorDto errorDto = new ErrorDto();
        if(from == null || to == null) {
            return errorHandlerUserList(responseDto,errorDto,"Exception! Given query params is null!",HttpStatus.BAD_REQUEST);
        }
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        if(fromDate.isAfter(toDate)) {
            return errorHandlerUserList(responseDto,errorDto,"Exception! Given 'from' query more than 'to' query!",HttpStatus.BAD_REQUEST);
        }
        List<User> searchedUsers;
        try {
            searchedUsers = userRepository.searchUserByBirthDateBetween(fromDate, toDate);
        } catch (Exception e) {
            return errorHandlerUserList(responseDto,errorDto,e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<UserDto> userDtoList = new ArrayList<>();
        if(!searchedUsers.isEmpty()) {
            userDtoList = searchedUsers.stream().map(userMapper::toDto).toList();
        }
        return responseDto.fillParameters(HttpStatus.OK, HttpHeaders.EMPTY, userDtoList, null);
    }

    private ResponseDto<UserDto> errorHandler(ResponseDto<UserDto> responseDto, ErrorDto errorDto, String errorMessage, HttpStatus httpStatus) {
        log.error(errorMessage);
        errorDto.setStatus(httpStatus.value());
        errorDto.setDetail(errorMessage);
        return responseDto.fillParameters(httpStatus, HttpHeaders.EMPTY, null, errorDto);
    }
    private ResponseDto<List<UserDto>> errorHandlerUserList(ResponseDto<List<UserDto>> responseDto, ErrorDto errorDto, String errorMessage, HttpStatus httpStatus) {
        log.error(errorMessage);
        errorDto.setStatus(httpStatus.value());
        errorDto.setDetail(errorMessage);
        return responseDto.fillParameters(httpStatus, HttpHeaders.EMPTY, null, errorDto);
    }
    private User updateExistedFields(UserDto userDto, User userToChange) {
        if (userDto.hasAddress()) {
            userToChange.setAddress(userDto.getAddress());
        }
        if (userDto.hasBirthDate()) {
            userToChange.setBirthDate(userDto.getBirthDate());
        }
        if (userDto.hasFirstName()) {
            userToChange.setFirstName(userDto.getFirstName());
        }
        if (userDto.hasLastName()) {
            userToChange.setLastName(userDto.getLastName());
        }
        if (userDto.hasPhoneNumber()) {
            userToChange.setPhoneNumber(userDto.getPhoneNumber());
        }
        if (userDto.hasEmail()) {
            userToChange.setEmail(userDto.getEmail());
        }
        return userToChange;
    }
}
