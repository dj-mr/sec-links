/**
 * This is the entry point for this application.
 */
package com.probemore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinancialDataApplication {

	/**
	 * This is the ebtry point method for this application
	 * @param args - Arguments passed in command line
	 */
	public static void main(String[] args) {
		SpringApplication.run(FinancialDataApplication.class, args);
	}

}