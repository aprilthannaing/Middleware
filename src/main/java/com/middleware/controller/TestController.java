package com.middleware.controller;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonView;
import com.middleware.entity.Views;

@RestController
@RequestMapping("")
public class TestController extends AbstractController {

	@Value("${SERVICEURL}")
	private String SERVICEURL;

	private static Logger logger = Logger.getLogger(TestController.class);

	@RequestMapping(value = "", method = RequestMethod.GET) /* WIPO REST end point 2.1 */
	@JsonView(Views.Summary.class)
	public String test(@RequestHeader("token") String token) throws Exception {

		if (!check(token))
			return "401";

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			String serviceUrl = SERVICEURL + "/test";

			RestTemplate restTemplate = new RestTemplate();
			final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			final HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
			factory.setHttpClient(httpClient);
			restTemplate.setRequestFactory(factory);

			ResponseEntity<String> response = restTemplate.exchange(serviceUrl, HttpMethod.GET, entity, String.class);
			logger.info("otherserviceResponse......" + response.getStatusCode().toString());

			return response.getStatusCode().toString();
		} catch (Exception e) {
			return "500";
		}
	}

	@RequestMapping(value = "test", method = RequestMethod.GET)
	@JsonView(Views.Summary.class)
	public String testRunning() {
		return "yes";
	}

	@RequestMapping(value = "authenticate", method = RequestMethod.GET) /* WIPO REST end point 2.6 */
	@JsonView(Views.Summary.class)
	public String authenticate(@RequestHeader("token") String token) throws Exception {
		if (!check(token))
			return "fail";

		return "success";
	}

}
