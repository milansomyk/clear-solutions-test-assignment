package milansomyk.testassignment.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import milansomyk.testassignment.constants.Constants;
import milansomyk.testassignment.dto.*;
import milansomyk.testassignment.entity.User;
import milansomyk.testassignment.mapper.UserMapper;
import milansomyk.testassignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final UserRepository userRepository;

    public ResponseDto<UserDto> createUser(RequestDto<UserDto> requestDto) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        ErrorDto errorDto = new ErrorDto();
        User user = userMapper.fromDto(requestDto.getData());
        user.setUuid(UUID.randomUUID());
        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            errorDto.fillParameters(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception while trying to save a user! Error: " + e.getMessage());
            return responseDto.setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorDto);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(Constants.USER_ENDPOINT + "/" + savedUser.getUuid()));
        return responseDto.fillParameters(HttpStatus.CREATED, httpHeaders, null, null, null, null);
    }

    public ResponseDto<UserDto> patchUser(RequestDto<UserDto> requestDto, UUID userUuid) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        ErrorDto errorDto = new ErrorDto();
        if (ObjectUtils.isEmpty(userUuid)) {
            log.error("Exception, given User id is null!");
            errorDto.fillParameters(HttpStatus.BAD_REQUEST.value(), "Exception, given User id is null!");
            return responseDto.setErrorResponse(HttpStatus.BAD_REQUEST, errorDto);
        }
        UserDto requestedUser = requestDto.getData();
        User savedUser;
        try {
            savedUser = userRepository.findByUuid(userUuid).orElse(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            errorDto.fillParameters(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception while trying to get a user! Error: " + e.getMessage());
            return responseDto.setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorDto);
        }
        if (savedUser == null) {
            log.error("Exception! User with this id: {} was not found!", userUuid);
            errorDto.fillParameters(HttpStatus.NOT_FOUND.value(), "Exception! User with this id: " + userUuid + " was not found!");
            return responseDto.setErrorResponse(HttpStatus.NOT_FOUND, errorDto);
        }
        User changedUser = updateExistedFields(requestedUser, savedUser);
        try {
            userRepository.save(changedUser);
        } catch (Exception e) {
            log.error("Exception while trying to save a user! Error: {}", e.getMessage());
            errorDto.fillParameters(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception while trying to save a user! Error: " + e.getMessage());
            return responseDto.setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorDto);
        }
        return responseDto.fillParameters(HttpStatus.OK, HttpHeaders.EMPTY, null, null, null, null);
    }

    public ResponseDto<UserDto> updateUser(RequestDto<UserDto> requestDto, UUID userUuid) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        ErrorDto errorDto = new ErrorDto();
        if (ObjectUtils.isEmpty(userUuid)) {
            log.error("Exception, given User id is null!");
            errorDto.fillParameters(HttpStatus.BAD_REQUEST.value(), "Exception, given User id is null!");
            return responseDto.setErrorResponse(HttpStatus.BAD_REQUEST, errorDto);
        }
        UserDto requestedUser = requestDto.getData();
        User savedUser;
        try {
            savedUser = userRepository.findByUuid(userUuid).orElse(null);
        } catch (Exception e) {
            log.error("Exception while trying to save user! Error: {}", e.getMessage());
            errorDto.fillParameters(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception while trying to save user! Error: " + e.getMessage());
            return responseDto.setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorDto);
        }
        if (savedUser == null) {
            log.error("Exception! User with this id: {} was not found!", userUuid);
            errorDto.fillParameters(HttpStatus.NOT_FOUND.value(), "Exception! User with this id: " + userUuid + " was not found!");
            return responseDto.setErrorResponse(HttpStatus.NOT_FOUND, errorDto);
        }
        updateExistedFields(requestedUser, savedUser);
        try {
            userRepository.save(savedUser);
        } catch (Exception e) {
            log.error("Exception while trying to save a user! Error: {}", e.getMessage());
            errorDto.fillParameters(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception while trying to save a user! Error: " + e.getMessage());
            return responseDto.setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorDto);
        }
        return responseDto.fillParameters(HttpStatus.OK, HttpHeaders.EMPTY, null, null, null, null);
    }

    public ResponseDto<UserDto> deleteUser(UUID userUuid) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        ErrorDto errorDto = new ErrorDto();
        if (ObjectUtils.isEmpty(userUuid)) {
            log.error("Exception, given user id is null!");
            errorDto.fillParameters(HttpStatus.BAD_REQUEST.value(), "Exception, given user id is null!");
            return responseDto.setErrorResponse(HttpStatus.BAD_REQUEST, errorDto);
        }
        User foundUser;
        try{
            foundUser = userRepository.findByUuid(userUuid).orElse(null);
        }catch(Exception e){
            log.error("Exception while trying to find user! Error: {}",e.getMessage());
            errorDto.fillParameters(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception while trying to find user! Error: " + e.getMessage());
            return responseDto.setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorDto);
        }
        if(foundUser == null){
            log.error("Exception! User with this id: {} was not found!", userUuid);
            errorDto.fillParameters(HttpStatus.BAD_REQUEST.value(), "Exception! User with this id: " + userUuid + " was not found!");
            return responseDto.setErrorResponse(HttpStatus.BAD_REQUEST, errorDto);
        }
        try {
            userRepository.deleteById(foundUser.getId());
        } catch (Exception e) {
            log.error("Exception while trying to delete a user! Error: {}", e.getMessage());
            errorDto.fillParameters(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception while trying to delete a user! Error: " + e.getMessage());
            return responseDto.setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorDto);
        }
        return responseDto.fillParameters(HttpStatus.OK, HttpHeaders.EMPTY, null, null, null, null);
    }

    public ResponseDto<List<UserDto>> searchUsersByBirthDateRange(String from, String to, Integer offset, Integer limit) {
        ResponseDto<List<UserDto>> responseDto = new ResponseDto<>();
        ErrorDto errorDto = new ErrorDto();
        if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to)) {
            log.error("Exception! Given query params is null!");
            errorDto.fillParameters(HttpStatus.BAD_REQUEST.value(), "Exception! Given query params is null!");
            return responseDto.setErrorResponse(HttpStatus.BAD_REQUEST, errorDto);
        }
        LocalDate fromDate;
        LocalDate toDate;
        try {
            fromDate = LocalDate.parse(from);
            toDate = LocalDate.parse(to);
        } catch (Exception e) {
            log.error("Exception while trying to parse the query params! Error: {}", e.getMessage());
            errorDto.fillParameters(HttpStatus.BAD_REQUEST.value(), "Exception while trying to parse the query params! Error: " + e.getMessage());
            return responseDto.setErrorResponse(HttpStatus.BAD_REQUEST, errorDto);
        }
        if (fromDate.isAfter(toDate)) {
            log.error("Exception! Given 'from' query more than 'to' query!");
            errorDto.fillParameters(HttpStatus.BAD_REQUEST.value(), "Exception! Given 'from' query more than 'to' query!");
            return responseDto.setErrorResponse(HttpStatus.BAD_REQUEST, errorDto);
        }
        List<User> searchedUsers;
        try {
            searchedUsers = userRepository.searchUserByBirthDateRange(fromDate, toDate, limit, offset);
        } catch (Exception e) {
            log.error("Exception while trying to get searched users! Error: {}", e.getMessage());
            errorDto.fillParameters(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception while trying to get searched users! Error: " + e.getMessage());
            return responseDto.setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorDto);
        }
        Integer totalSearchedUsers;
        try {
            totalSearchedUsers = userRepository.countAllByBirthDateBetween(fromDate, toDate);
        } catch (Exception e) {
            log.error("Exception while trying to get searched users total number! Error: {}", e.getMessage());
            errorDto.fillParameters(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception while trying to get searched users total number! Error: " + e.getMessage());
            return responseDto.setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorDto);
        }
        List<UserDto> userDtoList = new ArrayList<>();
        if (!searchedUsers.isEmpty()) {
            userDtoList = searchedUsers.stream().map(userMapper::toDto).toList();
        }

        PaginationLinks paginationLinks = new PaginationLinks();
        if (offset - limit >= 0) {
            paginationLinks.setPrev(Constants.USER_ENDPOINT + "?from=" + from + "&to=" + to + "&offset=" + (offset - limit) + "&limit=" + limit);
        }
        if (offset + limit < totalSearchedUsers) {
            paginationLinks.setNext(Constants.USER_ENDPOINT + "?from=" + from + "&to=" + to + "&offset=" + (offset + limit) + "&limit=" + limit);
        }

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setLimit(limit);
        paginationInfo.setOffset(offset);
        paginationInfo.setTotal(totalSearchedUsers);
        return responseDto.fillParameters(HttpStatus.OK, HttpHeaders.EMPTY, userDtoList, null, paginationLinks, paginationInfo);
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
