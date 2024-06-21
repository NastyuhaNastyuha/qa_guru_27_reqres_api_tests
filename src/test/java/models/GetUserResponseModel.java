package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetUserResponseModel {

    ResponseData data;
    SupportData support;

    @Data
    public static class ResponseData {
        String id, email, avatar;

        @JsonProperty("first_name")
        String firstName;

        @JsonProperty("last_name")
        String lastName;
    }

    @Data
    public static class SupportData {
        String url, text;
    }
}
