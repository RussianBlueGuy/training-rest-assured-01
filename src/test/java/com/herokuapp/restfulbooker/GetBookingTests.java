package com.herokuapp.restfulbooker;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetBookingTests extends BaseTest {

	@Test
	public void getBookingTest() {
		// Get response with booking
		Response response = RestAssured.given(spec).get("/booking/1");
		response.print();

		// Verify response 200 - hard assert because if code is not 200 then test should
		// stop
		Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not.");

		// Verify all fields - soft assert to verify each field... needs assertAll() at the end (see below)
		SoftAssert softAssert = new SoftAssert();
		String actualFirstName = response.jsonPath().getString("firstname");
		softAssert.assertEquals(actualFirstName, "Sally", "firstname in response is not expected");

		String actualLastName = response.jsonPath().getString("lastname");
		softAssert.assertEquals(actualLastName, "Wilson", "lastname in response is not expected");

		int price = response.jsonPath().getInt("totalprice");
		softAssert.assertEquals(price, 464, "totalprice in response is not expected");

		boolean depositPaid = response.jsonPath().getBoolean("depositpaid");
		softAssert.assertFalse(depositPaid, "depositpaid should be false but it's not");
//		 softAssert.assertTrue(depositPaid, "depositpaid should be true but it's not");

		String actualCheckin = response.jsonPath().getString("bookingdates.checkin");
		softAssert.assertEquals(actualCheckin, "2016-11-11", "checkin in response is not expected");

		String actualCheckout = response.jsonPath().getString("bookingdates.checkout");
		softAssert.assertEquals(actualCheckout, "2017-02-26", "checkout in response is not expected");
		
//		String actualAdditionalNeeds = response.jsonPath().getString("additionalneeds");
//		softAssert.assertEquals(actualAdditionalNeeds, "Breakfast", "additionalneeds in response is not expected");
		
		// this is required for Soft Assert, otherwise it will give false pass on test
		softAssert.assertAll();

	}

}
