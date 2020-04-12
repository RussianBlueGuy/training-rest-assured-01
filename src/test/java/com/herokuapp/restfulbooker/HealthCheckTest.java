package com.herokuapp.restfulbooker;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public class HealthCheckTest extends BaseTest {
	
	@Test
	public void healthCheckTest() {
		
		given().
			spec(spec).
		when().
			get("/ping").
		then().
			assertThat().
			statusCode(201);
	}
	
	@Test
	public void headersAndCookiesTest() {
		// one way to add header to the specification directly, before the response
		Header someHeader = new Header("some header name", "some header value");
		spec.header(someHeader);
		
		// one way to add a cookie to the specification directly, before the response
		Cookie someCookie = new Cookie.Builder("some cookie name", "some cookie value").build();
		spec.cookie(someCookie);
		
		// another way of adding cookie and another header to the request 
		Response response = RestAssured.given(spec).
				cookie("Test cookie name", "Test cookie value").
				header("Test header name", "Test header value").log().all().
				get("/ping");
		
		// Get all headers
		Headers headers = response.getHeaders();
		System.out.println("Headers: \n" + headers);
		
		// Get single header - one way... using the headers variable above
		Header serverHeader1 = headers.get("Server");
		System.out.println(serverHeader1.getName() + ": " + serverHeader1.getValue());
		
		// Get single header - another way... using response directly
		String serverHeader2 = response.getHeader("Connection");
		System.out.println("Connection: " + serverHeader2);
		
		// Get all cookies
		Cookies cookies = response.getDetailedCookies();
		System.out.println("Cookies: \n" + cookies);
		
	}

}
