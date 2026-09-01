package com.jse.invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class InvoiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(InvoiceApplication.class, args);
	}
}
