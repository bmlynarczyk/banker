package it.introsoft.banker.controller

import groovy.util.logging.Slf4j
import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import it.introsoft.banker.repository.TransferRepository
import org.apache.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static io.restassured.http.ContentType.JSON
import static org.hamcrest.Matchers.equalTo
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
@ContextConfiguration
class TransferControllerTest extends Specification {

    @LocalServerPort
    private int port

    @Autowired
    private TransferRepository transferRepository

    def setup() throws Exception {
        RestAssured.port = port
    }

    @Sql("../../../../transfers.sql")
    def "get all transfers"() {
        given:
        RequestSpecification request = given().accept(JSON)
        when:
        Response response = request.get('/api/transfers')
        then:
        response.statusCode() == HttpStatus.SC_OK
        !response.asString().isEmpty()
        response.then().body('_embedded.transfers.size()', equalTo(2));
        response.then().body('page.totalPages', equalTo(1));
        cleanup:
        transferRepository.deleteAll()
    }

}
