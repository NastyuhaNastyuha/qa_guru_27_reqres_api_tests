package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplate;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.*;

public class ReqresSpec {
    public static final RequestSpecification requestSpec = with()
            .filter(withCustomTemplate())
            .log().all()
            .contentType(ContentType.JSON);

    public static final ResponseSpecification responseStatusCode201Spec = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .log(ALL)
            .build();

    public static final ResponseSpecification responseStatusCode200Spec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(ALL)
            .build();

    public static final ResponseSpecification responseStatusCode204Spec = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .log(ALL)
            .build();

    public static final ResponseSpecification responseStatusCode400Spec = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .log(ALL)
            .build();
}