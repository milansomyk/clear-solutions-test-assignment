package milansomyk.testassignment.mapper;

import milansomyk.testassignment.dto.UserDto;
import milansomyk.testassignment.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder().id(user.getId()).email(user.getEmail()).firstName(user.getFirstName()).lastName(user.getLastName()).birthDate(user.getBirthDate()).address(user.getAddress()).phoneNumber(user.getPhoneNumber()).build();
    }

    public User fromDto(UserDto userDto) {
        return new User(userDto.getId(), userDto.getEmail(), userDto.getFirstName(), userDto.getLastName(), userDto.getBirthDate(), userDto.getAddress(), userDto.getPhoneNumber());
    }
}
