package com.springboot.onlinestore.service.impl;

import com.springboot.onlinestore.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class ScheduledServiceImpl {

	private final ProductService productService;

	@Scheduled(cron = "0 0 0 1 * *")
	public void performScheduledService() {
		log.info("Starting scheduled task to update date received product");
		productService.updateDateReceived();
		log.info("Finished scheduled task to update date received");
	}
}

