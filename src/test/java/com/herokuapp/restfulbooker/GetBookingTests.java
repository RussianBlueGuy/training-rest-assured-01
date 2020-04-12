package com.herokuapp.restfulbooker;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetBookingTests extends BaseTest {

	@Test
	public void getBookingTest() {
		
		// createBooking() in BaseTest Class in src/main/java folder
		Response responseCreate = createBooking();
		
		// Print responseCreate to console
		responseCreate.prettyPrint();
		
		// Get the id from the reponse to use it in the update
		int bookingId = responseCreate.jsonPath().getInt("bookingid");
		
		// Set path parameter
		spec.pathParam("bookingId", bookingId);
		
		// Get response with booking
		Response responseGet = RestAssured.given(spec).get("/booking/{bookingId}");
		responseGet.prettyPrint();
		//responseGet.print();

		// Verify response 200 - hard assert because if code is not 200 then test should
		// stop
		Assert.assertEquals(responseGet.getStatusCode(), 200, "Status code should be 200, but it's not.");

		// Verify all fields - soft assert to verify each field... needs assertAll() at the end (see below)
		SoftAssert softAssert = new SoftAssert();
		String actualFirstName = responseGet.jsonPath().getString("firstname");
		softAssert.assertEquals(actualFirstName, "Daffy", "firstname in response is not expected");

		String actualLastName = responseGet.jsonPath().getString("lastname");
		softAssert.assertEquals(actualLastName, "Duck", "lastname in response is not expected");

		int price = responseGet.jsonPath().getInt("totalprice");
		softAssert.assertEquals(price, 150, "totalprice in response is not expected");

		boolean depositPaid = responseGet.jsonPath().getBoolean("depositpaid");
//		softAssert.assertFalse(depositPaid, "depositpaid should be false but it's not");
		softAssert.assertTrue(depositPaid, "depositpaid should be true but it's not");

		String actualCheckin = responseGet.jsonPath().getString("bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2020-03-25", "checkin in response is not expected");

		String actualCheckout = responseGet.jsonPath().getString("bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2020-03-27", "checkout in response is not expected");
		
		String actualAdditionalNeeds = responseGet.jsonPath().getString("additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "Baby crib", "additionalneeds in response is not expected");
		
		// this is required for Soft Assert, otherwise it will give false pass on test
		softAssert.assertAll();

	}

}
