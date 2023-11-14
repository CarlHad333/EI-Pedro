package com.example.ei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class EiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EiApplication.class, args);
	}

}

// Add the controller.
@RestController
class HelloWorldController {
	@GetMapping("/")
	public String hello() {
		return "hello world!";
	}
}