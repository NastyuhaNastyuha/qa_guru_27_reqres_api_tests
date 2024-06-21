package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class UpdateUserBodyModel {
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String name;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String job;
}
