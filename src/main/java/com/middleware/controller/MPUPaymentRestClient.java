package com.middleware.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.middleware.entity.Payment;
import com.middleware.entity.Views;

public class MPUPaymentRestClient {

	private static Logger logger = Logger.getLogger(MPUPaymentRestClient.class);

	private static String serviceUrl = "http://122.248.120.252:60145/UAT/Payment/Payment/pay";

	public ResponseEntity<String> getForm(Payment payment) throws IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(payment);

		logger.info("Request JSON =" + requestJson);
		HttpEntity<String> entity = new HttpEntity<String>(requestJson.toString(), headers);

		RestTemplate restTemplate = new RestTemplate();
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		final HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
		factory.setHttpClient(httpClient);
		restTemplate.setRequestFactory(factory);
		
		ResponseEntity<String> response = restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, String.class);

		logger.info("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
		logger.info("response: " + response);
		return response;
	}

	public String thirdpartyAPI(Payment payment) throws IOException {
		String url = "http://122.248.120.252:60145/UAT/Payment/Payment/pay";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestMethod("POST");
		con.setDoOutput(true);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(payment);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(requestJson);
		wr.flush();
		wr.close();

		String responseUrl = "";
		int responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);
		if (responseCode >= 200 && responseCode < 300) {
			responseUrl = con.toString().split("HttpURLConnection:")[1];
			System.out.println("URL Content____" + con.getInputStream());
			System.out.println("URL Content____" + obj.toString());
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return responseUrl;
	}
	
}
