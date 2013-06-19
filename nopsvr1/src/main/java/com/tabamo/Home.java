package com.tabamo;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/subscription" })
public class Home {
	private final Logger logger = Logger.getLogger(Home.class);

	@RequestMapping(value = { "/gpbundle/{recurringPaymentNo}" }, method = { org.springframework.web.bind.annotation.RequestMethod.PUT }, consumes = { "application/json" })
	@ResponseBody
	public String register(
			@RequestHeader MultiValueMap<String, String> headers,
			@PathVariable("recurringPaymentNo") String recurringPaymentNo,
			@RequestBody String request) {
		this.logger.debug(headers);
		this.logger.debug(request);

		return "{\"error\": \"MESSAGE\"}";
	}

	@RequestMapping(value = { "/gpbundle/{recurringPaymentNo}/payment" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST }, consumes = { "application/json" })
	@ResponseBody
	public String payment(@RequestHeader MultiValueMap<String, String> headers,
			@PathVariable("recurringPaymentNo") String recurringPaymentNo,
			@RequestBody String request) {
		this.logger.debug(headers);
		this.logger.debug(request);

		
		
		return "{\"error\": \"MESSAGE\",\"paymentId\":"+ String.valueOf(new Random(System.currentTimeMillis()).nextInt())  +"}";
	}

	@RequestMapping(value = { "/gpbundle/{recurringPaymentNo}/payment/{purchaseNo}" }, method = { org.springframework.web.bind.annotation.RequestMethod.DELETE }, consumes = { "application/json" })
	@ResponseBody
	public String cancel(@RequestHeader MultiValueMap<String, String> headers,
			@PathVariable("recurringPaymentNo") String recurringPaymentNo,
			@PathVariable("purchaseNo") String purchaseNo,
			@RequestBody String request) {
		this.logger.debug(headers);
		this.logger.debug(request);

		return "ok";
	}
}