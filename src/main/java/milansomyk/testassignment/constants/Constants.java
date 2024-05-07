package milansomyk.testassignment.constants;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
    @Value("${user.minAge}")
    private Integer minAge;
    public static Integer USER_MIN_AGE;
    public static final String USER_ENDPOINT = "/users";
    @PostConstruct
    public void init(){
        USER_MIN_AGE = minAge;
    }
}
