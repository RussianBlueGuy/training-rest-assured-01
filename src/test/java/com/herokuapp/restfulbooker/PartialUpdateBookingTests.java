package com.herokuapp.restfulbooker;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PartialUpdateBookingTests extends BaseTest {

	@Test
	public void partialUpdateBookingTest() {
		// createBooking() in BaseTest Class in src/main/java folder
		Response responseCreate = createBooking();
		
		// Print responseCreate to console
		responseCreate.prettyPrint();
		
		// Get the id from the reponse to use it in the partial update
		int bookingId = responseCreate.jsonPath().getInt("bookingid");
		
		// Create JSON body with new data - only with some data that will be used in partial update
		JSONObject body = new JSONObject();
		body.put("firstname", "Porky");
		body.put("lastname", "Pig");

		JSONObject bookingDates = new JSONObject();
		bookingDates.put("checkin", "2020-04-25");
		bookingDates.put("checkout", "2020-04-27");
		body.put("bookingdates", bookingDates);

		// PATCH - Partial Update booking with new body from above, using id
		Response responseUpdate = RestAssured.given().auth().preemptive().basic("admin", "password123")
				.contentType(ContentType.JSON).body(body.toString())
				.patch("https://restful-booker.herokuapp.com/booking/" + bookingId);
		
		// Print responseUpdate to console
		responseUpdate.prettyPrint();		

		// Verifications
		// Verify response 200 - hard assert because if code is not 200 then test should stop
		Assert.assertEquals(responseUpdate.getStatusCode(), 200, "Status code should be 200, but it's not.");

		// Verify all fields - soft assert to verify each field... needs assertAll() at the end (see below)
		SoftAssert softAssert = new SoftAssert();
		String actualFirstName = responseUpdate.jsonPath().getString("firstname");
		softAssert.assertEquals(actualFirstName, "Porky", "firstname in response is not expected");

		String actualLastName = responseUpdate.jsonPath().getString("lastname");
		softAssert.assertEquals(actualLastName, "Pig", "lastname in response is not expected");

		int price = responseUpdate.jsonPath().getInt("totalprice");
		softAssert.assertEquals(price, 150, "totalprice in response is not expected");

		boolean depositPaid = responseUpdate.jsonPath().getBoolean("depositpaid");
//		softAssert.assertFalse(depositPaid, "depositpaid should be false but it's not");
		 softAssert.assertTrue(depositPaid, "depositpaid should be true but it's not");

		String actualCheckin = responseUpdate.jsonPath().getString("bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2020-04-25", "checkin in response is not expected");

		String actualCheckout = responseUpdate.jsonPath().getString("bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2020-04-27", "checkout in response is not expected");
		
		String actualAdditionalNeeds = responseUpdate.jsonPath().getString("additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "Baby crib", "additionalneeds in response is not expected");
		
		// this is required for Soft Assert, otherwise it will give false pass on test
		softAssert.assertAll();
		
	}

}
