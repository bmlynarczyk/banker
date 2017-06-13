package it.introsoft.banker.controller

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification
import spock.lang.Stepwise

import static io.restassured.RestAssured.given
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration
@Stepwise
class ImportControllerTest extends Specification {

    @LocalServerPort
    private int port

    def setup() throws Exception {
        RestAssured.port = port
    }

    @Sql("../../../../accounts.sql")
    def "account balance should be 0"() {
        given:
        RequestSpecification request = given()

        when:
        Response response = request.when().get('/accounts')

        then:
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("_embedded.accounts.size()", CoreMatchers.is(1))
                .body("_embedded.accounts[0].currentBalance", CoreMatchers.is(0))

    }

    def "shouldn't import transfers when unknown account"() {
        given:
        RequestSpecification request = given()
                .queryParam('filePath', 'src/test/resources/history.html')
                .queryParam('account', '11 1020 3176 0000 0000 0000')

        when:
        Response response = request.when().post('/import')

        then:
        response.statusCode() == HttpStatus.SC_BAD_REQUEST
    }

    def "shouldn't import transfers when file doesn't exists"() {
        given:
        RequestSpecification request = given()
                .queryParam('filePath', 'src/test/resources/tory.html')
                .queryParam('account', '11 1020 3176 0000 0000 0000 0000')

        when:
        Response response = request.when().post('/import')

        then:
        response.statusCode() == HttpStatus.SC_BAD_REQUEST
    }

    def "should import pkobp transfers"() {
        given:
        RequestSpecification request = given()
                .queryParam('filePath', 'src/test/resources/history.html')
                .queryParam('account', '11 1020 3176 0000 0000 0000 0000')

        when:
        Response response = request.when().post('/import')

        then:
        response.asString().isEmpty()
        response.statusCode() == HttpStatus.SC_OK
    }

    def "should find imported pkobp transfers"() {
        given:
        RequestSpecification request = given()

        when:
        Response response = request.when().get('/transfers')

        then:
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("_embedded.transfers.size()", CoreMatchers.is(2))

    }

    def "should update account balance"() {
        given:
        RequestSpecification request = given()

        when:
        Response response = request.when().get('/accounts')

        then:
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("_embedded.accounts.size()", CoreMatchers.is(1))
                .body("_embedded.accounts[0].currentBalance", CoreMatchers.is(16243360))

    }

}