package com.nutrifit.useraccount;

import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Sql(scripts = "/testdata/create_schema_and_insert_user_accounts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class UserAccountServiceApplicationTests {

	@Autowired
	MySQLContainer<?> mySQLContainer;

	@LocalServerPort
	Integer serverPort;

	@BeforeEach
	void setup() {

		RestAssured.baseURI = "http://localhost";
		RestAssured.port = serverPort;
	}

	@Test
	void shouldCreateUser_whenPutRequestIsValid() {

		String requestBody = """
				{
					"username": "testuser",
					"email": "testuser@gmail.com",
					"password": "testpassword",
					"firstName": "Test",
					"lastName": "User"
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
			.when()
				.post("/api/users")
			.then()
				.statusCode(HttpStatus.CREATED.value())
				.body("username", Matchers.equalTo("testuser"))
				.body("email", Matchers.equalTo("testuser@gmail.com"))
				.body("firstName", Matchers.equalTo("Test"))
				.body("lastName", Matchers.equalTo("User"));
	}

	@Test
	void shouldReturnAllUsers_whenGetRequestIsMade() {

		RestAssured.given()
			.when()
				.get("/api/users")
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("size()", Matchers.greaterThan(0))
				.body("[0].username", Matchers.notNullValue())
				.body("[0].email", Matchers.notNullValue())
				.body("[0].firstName", Matchers.notNullValue())
				.body("[0].lastName", Matchers.notNullValue());
	}

	@Test
	void shouldNotCreateUser_whenPutRequestIsInvalid() {

		String requestBody = """
				{
					"username": "",
					"email": "invalidemail",
					"password": "short",
					"firstName": "",
					"lastName": ""
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
			.when()
				.post("/api/users")
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("errors", Matchers.hasSize(Matchers.greaterThan(0)))
				.body("errors.message", Matchers.hasItem("Username cannot be blank"))
				.body("errors.message", Matchers.hasItem("Email is not valid"))
				.body("errors.message", Matchers.hasItem("First name cannot be blank"))
				.body("errors.message", Matchers.hasItem("Last name cannot be blank"));
	}

	@Test
	public void shouldReturnUserById_whenGetRequestIsMade() {

		String userId = "f3c6a214-7d7e-4d29-a9a9-58d6e1b623e0"; // Assuming the first user has this ID

		RestAssured.given()
			.when()
				.get("/api/users/{id}", userId)
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("id", Matchers.notNullValue())
				.body("username", Matchers.notNullValue())
				.body("email", Matchers.notNullValue())
				.body("firstName", Matchers.notNullValue())
				.body("lastName", Matchers.notNullValue());
	}

	@Test
	public void shouldReturnNotFound_whenUserDoesNotExist() {

		String nonExistentUserId = "00000000-0000-0000-0000-000000000000"; // Non-existent UUID

		RestAssured.given()
			.when()
				.get("/api/users/{id}", nonExistentUserId)
			.then()
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body("message", Matchers.equalTo("User not found"));
	}

	@Test
	public void shouldUpdateUser_whenPutRequestIsValid() {

		String userId = "f3c6a214-7d7e-4d29-a9a9-58d6e1b623e0"; // Assuming the first user has this ID

		String requestBody = """
				{
					"username": "updateduser",
					"email": "updateduser@gmail.com",
					"password": "updatedpassword",
					"firstName": "Updated",
					"lastName": "User"
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
			.when()
				.put("/api/users/{id}", userId)
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("username", Matchers.equalTo("updateduser"))
				.body("email", Matchers.equalTo("updateduser@gmail.com"))
				.body("firstName", Matchers.equalTo("Updated"))
				.body("lastName", Matchers.equalTo("User"));

	}

	@Test
	public void shouldDeleteUser_whenDeleteRequestIsMade() {

		String userId = "f3c6a214-7d7e-4d29-a9a9-58d6e1b623e0"; // Assuming the first user has this ID

		RestAssured.given()
				.contentType("application/json")
			.when()
				.delete("/api/users/{id}", userId)
			.then()
				.statusCode(HttpStatus.NO_CONTENT.value());

		RestAssured.given()
				.contentType("application/json")
			.when()
				.get("/api/users/{id}", userId)
			.then()
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body("message", Matchers.equalTo("User not found"));
	}

}
