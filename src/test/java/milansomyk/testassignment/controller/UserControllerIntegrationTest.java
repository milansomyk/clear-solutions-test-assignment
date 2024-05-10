package milansomyk.testassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import milansomyk.testassignment.dto.BodyDto;
import milansomyk.testassignment.dto.RequestDto;
import milansomyk.testassignment.dto.ResponseDto;
import milansomyk.testassignment.dto.UserDto;
import milansomyk.testassignment.service.UserService;

import milansomyk.testassignment.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerIntegrationTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private ValidationService validationService;
    @InjectMocks
    private UserController userController;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void UserController_GetUsersByBirthDate_ReturnUserList() throws Exception {
        ResponseDto<List<UserDto>> listResponseDto = new ResponseDto<>();
        UserDto userDto = new UserDto(null,"correct@gmail.com","CorrectName","CorrectLastname", LocalDate.of(2001, 2, 2),"CorrectStreet",123123123L);
        BodyDto<UserDto> userDtoBodyDto = new BodyDto<>();
        userDtoBodyDto.setPagination(null);
        userDtoBodyDto.setErrors(null);
        userDtoBodyDto.setData(userDto);
        userDtoBodyDto.setLinks(null);
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);
        listResponseDto.fillParameters(HttpStatus.OK, HttpHeaders.EMPTY,userDtoList,null,null,null);
        given(userService.searchUsersByBirthDateRange("2000-01-01","2002-01-01",0,100)).willReturn(listResponseDto);
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("from","2000-01-01")
                .queryParam("to","2002-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].email").value(userDto.getEmail()));
    }
    @Test
    public void UserController_CreateUser_ReturnUser() throws Exception  {
        RequestDto<UserDto> requestDto = new RequestDto<>();
        UserDto userDto = new UserDto(null,"correct@gmail.com","CorrectName","CorrectLastname", LocalDate.of(2001, 2, 2),"CorrectStreet",123123123L);
        requestDto.setData(userDto);
        requestDto.setHttpMethod(HttpMethod.POST);
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        BodyDto<UserDto> userDtoBodyDto = new BodyDto<>();
        userDtoBodyDto.setPagination(null);
        userDtoBodyDto.setErrors(null);
        userDtoBodyDto.setData(userDto);
        userDtoBodyDto.setLinks(null);
        responseDto.setBody(userDtoBodyDto);
        responseDto.setHttpHeaders(HttpHeaders.EMPTY);
        responseDto.setHttpStatus(HttpStatus.CREATED);
        given(userService.createUser(requestDto)).willReturn(responseDto);
        mockMvc.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }
    @Test
    public void UserController_UpdateUser_ReturnUser() throws Exception {
        RequestDto<UserDto> requestDto = new RequestDto<>();
        UUID uuid = UUID.randomUUID();
        UserDto userDto = new UserDto(null,"updated@gmail.com","UpdatedName","UpdatedLastName", LocalDate.of(1989, 10, 22),"UpdatedStreet",456456456L);
        requestDto.setData(userDto);
        requestDto.setHttpMethod(HttpMethod.PUT);
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        BodyDto<UserDto> userDtoBodyDto = new BodyDto<>();
        userDtoBodyDto.setPagination(null);
        userDtoBodyDto.setErrors(null);
        userDtoBodyDto.setData(userDto);
        userDtoBodyDto.setLinks(null);
        responseDto.setBody(userDtoBodyDto);
        responseDto.setHttpHeaders(HttpHeaders.EMPTY);
        responseDto.setHttpStatus(HttpStatus.OK);
        given(userService.updateUser(requestDto,uuid)).willReturn(responseDto);
        mockMvc.perform(put("/users/"+uuid)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(userDto.getEmail()));
    }
    @Test
    public void UserController_PatchUser_ReturnUser() throws Exception {
        RequestDto<UserDto> requestDto = new RequestDto<>();
        UUID uuid = UUID.randomUUID();
        UserDto userDto = new UserDto(null,"patched@gmail.com",null,"PatchedLastName", null,null,null);
        requestDto.setData(userDto);
        requestDto.setHttpMethod(HttpMethod.PATCH);
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        BodyDto<UserDto> userDtoBodyDto = new BodyDto<>();
        userDtoBodyDto.setPagination(null);
        userDtoBodyDto.setErrors(null);
        userDtoBodyDto.setData(new UserDto(null,"patched@gmail.com","UpdatedName","PatchedLastName", LocalDate.of(1989, 10, 22),"UpdatedStreet",456456456L));
        userDtoBodyDto.setLinks(null);
        responseDto.setBody(userDtoBodyDto);
        responseDto.setHttpHeaders(HttpHeaders.EMPTY);
        responseDto.setHttpStatus(HttpStatus.OK);
        given(validationService.validate(requestDto)).willReturn(new HashMap<>());
        given(userService.patchUser(requestDto,uuid)).willReturn(responseDto);
        mockMvc.perform(patch("/users/"+uuid)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(userDto.getEmail()));
    }
    @Test
    public void UserController_DeleteUser_ReturnVoid() throws Exception {
        UUID uuid = UUID.randomUUID();
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        BodyDto<UserDto> userDtoBodyDto = new BodyDto<>();
        userDtoBodyDto.setPagination(null);
        userDtoBodyDto.setErrors(null);
        userDtoBodyDto.setData(null);
        userDtoBodyDto.setLinks(null);
        responseDto.setBody(userDtoBodyDto);
        responseDto.setHttpHeaders(HttpHeaders.EMPTY);
        responseDto.setHttpStatus(HttpStatus.OK);
        given(userService.deleteUser(uuid)).willReturn(responseDto);
        mockMvc.perform(delete("/users/"+uuid))
                .andExpect(status().isOk());
    }

}
