import dao.StudentDao;
import entity.Student;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiTest extends TestBase {

    @Order(1)
    @Test
    public void shouldPostNewStudentAndValidateResponse() {
        DataStore.STUDENT_POST = given(getRequestSpecificationPOST())
                .when()
                .body(new File("src/main/resources/Student.json"))
                .post()
                .then()
                .spec(getResponseSpecificationPost())
                .extract()
                .as(Student.class);
    }

    @Order(2)
    @Test
    public void shouldGetCreatedStudentByIdAndValidateResponse() {
        DataStore.STUDENT_GET = given(getRequestSpecificationGET())
                .when()
                .pathParam("id", DataStore.STUDENT_POST.getId())
                .get()
                .then()
                .spec(getResponseSpecificationGet())
                .extract()
                .response()
                .jsonPath()
                .and()
                .getObject("data", Student.class);
    }

    @Order(3)
    @Test
    public void shouldGetCreatedStudentAndValidateWithDB() {
        assertThat("Wrong student details... "
                        + "\nExpected: " + StudentDao.findByName("Kamil")
                        + "\nActual: " + DataStore.STUDENT_GET,
                StudentDao.findByName("Kamil").equals(DataStore.STUDENT_GET));
    }
}