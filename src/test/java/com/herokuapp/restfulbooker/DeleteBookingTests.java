package com.herokuapp.restfulbooker;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DeleteBookingTests extends BaseTest {

	@Test
	public void deleteBookingTest() {
		// createBooking() in BaseTest Class in src/main/java folder
		Response responseCreate = createBooking();
		
		// Print responseCreate to console
		responseCreate.prettyPrint();
		
		// Get the id from the reponse to use it in the partial update
		int bookingId = responseCreate.jsonPath().getInt("bookingid");

		// DELETE - delete booking created above using id
		Response responseDelete = RestAssured.given().auth().preemptive().basic("admin", "password123")
				.delete("https://restful-booker.herokuapp.com/booking/" + bookingId);
		
		// Print responseUpdate to console
		responseDelete.prettyPrint();		

		// Verifications
		// Verify response 201 - hard assert because if code is not 200 then test should stop
		Assert.assertEquals(responseDelete.getStatusCode(), 201, "Status code should be 201, but it's not.");
		
		// Verify we're not able to get the booking using the id after the delete		
		Response responseGet = RestAssured.get("https://restful-booker.herokuapp.com/booking" + bookingId);
		
		// Print responseUpdate to console
		responseGet.prettyPrint();
		
		// Verify we get back string "Not Found"
		Assert.assertEquals(responseGet.body().asString(), "Not Found", "Body should be 'Not Found' but it's not");
		
	}

}
