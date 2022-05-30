import com.fasterxml.jackson.databind.ObjectMapper;
import dao.StudentDao;
import entity.Student;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestBase {

    @BeforeAll
    static void createStudent() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            DataStore.STUDENT_JSON = mapper.readValue(new File("src/main/resources/Student.json"), Student.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StudentDao.addStudent(DataStore.STUDENT_JSON);

//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    public static void deleteStudent() {
        given()
                .baseUri("http://thetestingworldapi.com")
                .basePath("/api/studentsDetails/{id}")
                .when()
                .pathParam("id", DataStore.STUDENT_POST.getId())
                .delete()
                .then()
                .statusCode(200);
    }

    @AfterAll
    static void shutDown() {
        deleteStudent();
    }

    public RequestSpecification getRequestSpecificationPOST() {
        return given()
                .baseUri("http://thetestingworldapi.com")
                .basePath("/api/studentsDetails")
                .contentType(ContentType.JSON)
                .log()
                .all();
    }

    public RequestSpecification getRequestSpecificationGET() {
        return given()
                .baseUri("http://thetestingworldapi.com")
                .basePath("/api/studentsDetails/{id}")
                .contentType(ContentType.JSON)
                .log()
                .all();
    }

    public ResponseSpecification getResponseSpecificationPost() {
        ResponseSpecification responseSpecification = RestAssured.expect();

        return responseSpecification
                .log()
                .all()
                .statusCode(201)
                .body("first_name", equalTo(DataStore.STUDENT_JSON.getFirst_name()))
                .body("middle_name", equalTo(DataStore.STUDENT_JSON.getMiddle_name()))
                .body("last_name", equalTo(DataStore.STUDENT_JSON.getLast_name()))
                .body("date_of_birth", equalTo(DataStore.STUDENT_JSON.getDate_of_birth()));
    }

    public ResponseSpecification getResponseSpecificationGet() {
        ResponseSpecification responseSpecification = RestAssured.expect();

        return responseSpecification
                .log()
                .all()
                .statusCode(200)
                .rootPath("data")
                .body("first_name", equalTo(DataStore.STUDENT_POST.getFirst_name()))
                .body("middle_name", equalTo(DataStore.STUDENT_POST.getMiddle_name()))
                .body("last_name", equalTo(DataStore.STUDENT_POST.getLast_name()))
                .body("date_of_birth", equalTo(DataStore.STUDENT_POST.getDate_of_birth()));
    }
}