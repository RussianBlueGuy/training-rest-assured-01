package com.herokuapp.restfulbooker;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CreateBookingTests extends BaseTest {

	@Test(enabled=false)
	public void createBookingTest() {
		// createBooking() in BaseTest Class in src/main/java folder
		Response response = createBooking();
		
		// Print response to console
		response.prettyPrint();

		// Verifications
		// Verify response 200 - hard assert because if code is not 200 then test should stop
		Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not.");

		// Verify all fields - soft assert to verify each field... needs assertAll() at the end (see below)
		SoftAssert softAssert = new SoftAssert();
		String actualFirstName = response.jsonPath().getString("booking.firstname");
		softAssert.assertEquals(actualFirstName, "Daffy", "firstname in response is not expected");

		String actualLastName = response.jsonPath().getString("booking.lastname");
		softAssert.assertEquals(actualLastName, "Duck", "lastname in response is not expected");

		int price = response.jsonPath().getInt("booking.totalprice");
		softAssert.assertEquals(price, 150, "totalprice in response is not expected");

		boolean depositPaid = response.jsonPath().getBoolean("booking.depositpaid");
//		softAssert.assertFalse(depositPaid, "depositpaid should be false but it's not");
		 softAssert.assertTrue(depositPaid, "depositpaid should be true but it's not");

		String actualCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2020-03-25", "checkin in response is not expected");

		String actualCheckout = response.jsonPath().getString("booking.bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2020-03-27", "checkout in response is not expected");
		
		String actualAdditionalNeeds = response.jsonPath().getString("booking.additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "Baby crib", "additionalneeds in response is not expected");
		
		// this is required for Soft Assert, otherwise it will give false pass on test
		softAssert.assertAll();
		
	}
	
	
	@Test
	public void createBookingWithPOJOTest() {
		// Instead of using JSON body, we will create body using the Booking and BookingDates POJOs
		BookingDates bookingdates = new BookingDates("2020-04-01", "2020-04-05");
		Booking booking = new Booking("Foghorn", "Leghorn", 245, true, bookingdates, "Chicks");

		// Get response
		Response response = RestAssured.given(spec).contentType(ContentType.JSON).body(booking.toString())
				.post("/booking");
		
		// Print response to console
		response.prettyPrint();

		// Verifications
		// Verify response 200 - hard assert because if code is not 200 then test should stop
		Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not.");

		// Verify all fields - soft assert to verify each field... needs assertAll() at the end (see below)
		SoftAssert softAssert = new SoftAssert();
		String actualFirstName = response.jsonPath().getString("booking.firstname");
		softAssert.assertEquals(actualFirstName, "Foghorn", "firstname in response is not expected");

		String actualLastName = response.jsonPath().getString("booking.lastname");
		softAssert.assertEquals(actualLastName, "Leghorn", "lastname in response is not expected");

		int price = response.jsonPath().getInt("booking.totalprice");
		softAssert.assertEquals(price, 245, "totalprice in response is not expected");

		boolean depositPaid = response.jsonPath().getBoolean("booking.depositpaid");
//		softAssert.assertFalse(depositPaid, "depositpaid should be false but it's not");
		 softAssert.assertTrue(depositPaid, "depositpaid should be true but it's not");

		String actualCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2020-04-01", "checkin in response is not expected");

		String actualCheckout = response.jsonPath().getString("booking.bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2020-04-05", "checkout in response is not expected");
		
		String actualAdditionalNeeds = response.jsonPath().getString("booking.additionalneeds");
		softAssert.assertEquals(actualAdditionalNeeds, "Chicks", "additionalneeds in response is not expected");
		
		// this is required for Soft Assert, otherwise it will give false pass on test
		softAssert.assertAll();
		
	}

}
