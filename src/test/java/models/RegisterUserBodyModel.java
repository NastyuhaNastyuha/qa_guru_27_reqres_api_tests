package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class RegisterUserBodyModel {
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String email;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String password;
}
