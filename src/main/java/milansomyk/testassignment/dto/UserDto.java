package milansomyk.testassignment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @JsonIgnore
    Integer id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    UUID uuid;
    @Email(message = "Validation Exception! Error: Email is not validated")
    String email;
    @Size(min = 2, max = 32, message = "Validation Exception! Error: First name must be between 2 and 32 characters long")
    String firstName;
    @Size(min = 2, max = 32, message = "Validation Exception! Error: Last name must be between 2 and 32 characters long")
    String lastName;
    @PastOrPresent(message = "Validation Exception! Error: Birth date may not be future date")
    LocalDate birthDate;
    @Size(min = 2, max = 32, message = "Validation Exception! Error: Address have to be between 2 and 32 letters")
    String address;
    @Digits(integer = 14, fraction = 0, message = "Validation Exception! Error: Phone number is too short or too long")
    Long phoneNumber;

    public boolean isAllRequired() {
        return this.firstName != null && this.lastName != null && this.birthDate != null && this.email != null;
    }

    public boolean isAnyRequired() {
        return this.firstName != null || this.lastName != null || this.birthDate != null || this.email != null || this.phoneNumber != null || this.address != null;
    }

    public boolean hasEmail() {
        return this.email != null;
    }

    public boolean hasFirstName() {
        return this.firstName != null;
    }

    public boolean hasLastName() {
        return this.lastName != null;
    }

    public boolean hasBirthDate() {
        return this.birthDate != null;
    }

    public boolean hasAddress() {
        return this.address != null;
    }

    public boolean hasPhoneNumber() {
        return this.phoneNumber != null;
    }
}
