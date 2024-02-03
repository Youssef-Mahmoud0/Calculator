package com.lab2.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController("/")
@CrossOrigin(origins = "*")
public class CalculatorApplication {
	public static void main(String[] args) {
		SpringApplication.run(CalculatorApplication.class, args);
	}


}
