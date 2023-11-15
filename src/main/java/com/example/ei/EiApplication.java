package com.example.ei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;
import java.sql.SQLException;

@SpringBootApplication
public class EiApplication {

	protected static final Object lock = new Object();

	public static void main(String[] args) {
			SpringApplication.run(EiApplication.class, args);
		}
}

// Add the controller.
@RestController
class HelloWorldController {
	@GetMapping("/")
	public String hello() {

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
}