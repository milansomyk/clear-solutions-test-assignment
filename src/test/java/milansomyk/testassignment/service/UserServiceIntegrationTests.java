package milansomyk.testassignment.service;

import milansomyk.testassignment.dto.BodyDto;
import milansomyk.testassignment.dto.RequestDto;
import milansomyk.testassignment.dto.ResponseDto;
import milansomyk.testassignment.dto.UserDto;
import milansomyk.testassignment.entity.User;
import milansomyk.testassignment.mapper.UserMapper;
import milansomyk.testassignment.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceIntegrationTests {
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void UserService_CreateUser_ReturnCreatedStatus() throws Exception {
        RequestDto<UserDto> requestDto = new RequestDto<>();
        UserDto userDto = new UserDto(1, UUID.randomUUID(), "correct@gmail.com", "CorrectName", "CorrectLastname", LocalDate.of(2001, 2, 2), "CorrectStreet", 123123123L);
        User user = new User(1, UUID.randomUUID(), "correct@gmail.com", "CorrectName", "CorrectLastname", LocalDate.of(2001, 2, 2), "CorrectStreet", 123123123L);
        requestDto.setData(userDto);
        requestDto.setHttpMethod(HttpMethod.POST);
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        BodyDto<UserDto> bodyDto = new BodyDto<>();

        bodyDto.setPagination(null);
        bodyDto.setErrors(null);
        bodyDto.setData(userDto);
        bodyDto.setLinks(null);

        responseDto.setBody(bodyDto);
        responseDto.setHttpHeaders(HttpHeaders.EMPTY);
        responseDto.setHttpStatus(HttpStatus.CREATED);

        when(userMapper.fromDto(requestDto.getData())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        ResponseDto<UserDto> user1 = userService.createUser(requestDto);
        verify(userRepository, times(1)).save(user);

        Assertions.assertThat(user1.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void UserService_PatchUser_ReturnOkStatus() throws Exception {
        RequestDto<UserDto> requestDto = new RequestDto<>();
        UUID uuid = UUID.randomUUID();
        UserDto userDto = new UserDto(4, uuid, "correct@gmail.com", "CorrectName", "CorrectLastname", LocalDate.of(2001, 2, 2), "CorrectStreet", 123123123L);
        User user = new User(4, uuid, "correct@gmail.com", "CorrectName", "CorrectLastname", LocalDate.of(2001, 2, 2), "CorrectStreet", 123123123L);
        requestDto.setData(userDto);
        requestDto.setHttpMethod(HttpMethod.PATCH);
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        BodyDto<UserDto> bodyDto = new BodyDto<>();

        bodyDto.setPagination(null);
        bodyDto.setErrors(null);
        bodyDto.setData(userDto);
        bodyDto.setLinks(null);

        responseDto.setBody(bodyDto);
        responseDto.setHttpHeaders(HttpHeaders.EMPTY);
        responseDto.setHttpStatus(HttpStatus.OK);

        when(userRepository.findByUuid(userDto.getUuid())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        ResponseDto<UserDto> user1 = userService.patchUser(requestDto, uuid);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findByUuid(userDto.getUuid());

        Assertions.assertThat(user1.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void UserService_UpdateUser_ReturnOkStatus() throws Exception {
        RequestDto<UserDto> requestDto = new RequestDto<>();
        UUID uuid = UUID.randomUUID();
        UserDto userDto = new UserDto(3, uuid, "correct@gmail.com", "CorrectName", "CorrectLastname", LocalDate.of(2001, 2, 2), "CorrectStreet", 123123123L);
        User user = new User(3, uuid, "correct@gmail.com", "CorrectName", "CorrectLastname", LocalDate.of(2001, 2, 2), "CorrectStreet", 123123123L);
        requestDto.setData(userDto);
        requestDto.setHttpMethod(HttpMethod.PUT);
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        BodyDto<UserDto> bodyDto = new BodyDto<>();

        bodyDto.setPagination(null);
        bodyDto.setErrors(null);
        bodyDto.setData(userDto);
        bodyDto.setLinks(null);

        responseDto.setBody(bodyDto);
        responseDto.setHttpHeaders(HttpHeaders.EMPTY);
        responseDto.setHttpStatus(HttpStatus.OK);

        when(userRepository.findByUuid(userDto.getUuid())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        ResponseDto<UserDto> user1 = userService.updateUser(requestDto, uuid);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findByUuid(userDto.getUuid());

        Assertions.assertThat(user1.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void UserService_DeleteUser_ReturnOkStatus() throws Exception {
        RequestDto<UserDto> requestDto = new RequestDto<>();
        UUID uuid = UUID.randomUUID();
        requestDto.setData(null);
        requestDto.setHttpMethod(HttpMethod.DELETE);
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        BodyDto<UserDto> bodyDto = new BodyDto<>();
        User user = new User(3, uuid, "correct@gmail.com", "CorrectName", "CorrectLastname", LocalDate.of(2001, 2, 2), "CorrectStreet", 123123123L);
        bodyDto.setPagination(null);
        bodyDto.setErrors(null);
        bodyDto.setData(null);
        bodyDto.setLinks(null);

        responseDto.setBody(bodyDto);
        responseDto.setHttpHeaders(HttpHeaders.EMPTY);
        responseDto.setHttpStatus(HttpStatus.OK);
        when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
        ResponseDto<UserDto> deleteResponseDto = userService.deleteUser(uuid);
        verify(userRepository, times(1)).deleteById(user.getId());

        Assertions.assertThat(deleteResponseDto.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void UserService_SearchUsersByBirthDateRange_ReturnOkStatus() throws Exception {
        RequestDto<List<UserDto>> listRequestDto = new RequestDto<>();
        UserDto userDto = new UserDto(2, UUID.randomUUID(), "correct@gmail.com", "CorrectName", "CorrectLastname", LocalDate.of(2001, 2, 2), "CorrectStreet", 123123123L);
        UserDto anotherUserDto = new UserDto(3, UUID.randomUUID(), "anotherCorrect@gmail.com", "anotherCorrectName", "anotherCorrectLastname", LocalDate.of(2000, 1, 15), "anotherCorrectStreet", 123145123L);
        List<UserDto> givenUsers = new ArrayList<>();
        givenUsers.add(userDto);
        givenUsers.add(anotherUserDto);
        User user = new User(2, UUID.randomUUID(), "correct@gmail.com", "CorrectName", "CorrectLastname", LocalDate.of(2001, 2, 2), "CorrectStreet", 123123123L);
        User anotherUser = new User(3, UUID.randomUUID(), "anotherCorrect@gmail.com", "anotherCorrectName", "anotherCorrectLastname", LocalDate.of(2000, 1, 15), "anotherCorrectStreet", 123145123L);
        List<User> foundUsers = new ArrayList<>();
        foundUsers.add(user);
        foundUsers.add(anotherUser);
        listRequestDto.setData(new ArrayList<>());
        listRequestDto.getData().add(userDto);
        listRequestDto.getData().add(anotherUserDto);
        listRequestDto.setHttpMethod(HttpMethod.PUT);
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        BodyDto<UserDto> bodyDto = new BodyDto<>();
        bodyDto.setPagination(null);
        bodyDto.setErrors(null);
        bodyDto.setData(userDto);
        bodyDto.setLinks(null);

        responseDto.setBody(bodyDto);
        responseDto.setHttpHeaders(HttpHeaders.EMPTY);
        responseDto.setHttpStatus(HttpStatus.OK);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(userMapper.toDto(anotherUser)).thenReturn(anotherUserDto);
        when(userRepository.searchUserByBirthDateRange(LocalDate.of(2000, 1, 1), LocalDate.of(2003, 1, 1), 100, 0)).thenReturn(foundUsers);
        when(userRepository.countAllByBirthDateBetween(LocalDate.of(2000, 1, 1), LocalDate.of(2003, 1, 1))).thenReturn(2);
        ResponseDto<List<UserDto>> listResponseDto = userService.searchUsersByBirthDateRange("2000-01-01", "2003-01-01", 0, 100);
        verify(userRepository, times(1)).searchUserByBirthDateRange(LocalDate.of(2000, 1, 1), LocalDate.of(2003, 1, 1), 100, 0);
        verify(userRepository, times(1)).countAllByBirthDateBetween(LocalDate.of(2000, 1, 1), LocalDate.of(2003, 1, 1));

        Assertions.assertThat(listResponseDto.getBody().getData()).isEqualTo(givenUsers);
        Assertions.assertThat(listResponseDto.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }
}
