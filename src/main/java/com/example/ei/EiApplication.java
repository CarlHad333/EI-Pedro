package com.example.ei;

import com.google.api.client.json.Json;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@SpringBootApplication
public class EiApplication {
	public static void main(String[] args) {
			SpringApplication.run(EiApplication.class, args);
		}
}

// Add the controller.
@RestController
class DataController {
	@GetMapping("/")
	public String savedata() {
		String res;
		JdbcMysqlJ8IntegrationTests j = new JdbcMysqlJ8IntegrationTests();
		try {
			j.setUpPool();
			res = j.pooledConnectionTest();
			j.dropTableIfPresent();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return res;
	}

	@GetMapping("/lol")
	public String recieveData1() {
		return "Data 1";
	}

	@PostMapping("/lol")
	public String recieveData(@RequestBody String data) {
		System.out.println("Received data: " + data);
		return "Data received successfully!";
	}

}