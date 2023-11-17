package com.example.ei;

import com.google.api.client.json.Json;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalTime;



@SpringBootApplication
public class EiApplication {
	public static void main(String[] args) {
			SpringApplication.run(EiApplication.class, args);
		}

}

// Add the controller.
@RestController
class DataController {

	JdbcMysqlJ8IntegrationTests db ;
	float i = 0;

	@PostConstruct
	public void init() throws SQLException {
		try {
			this.db = new JdbcMysqlJ8IntegrationTests();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	@GetMapping(value = "/delete")
	public String deletedata() throws SQLException {
		try	{
			db.dropTableIfPresent();
		} catch (SQLException e) {
			System.out.println(e);
		}
		return "Delete Successful";
    }

    @PostMapping(value = "/sql")
	public String savedata(@RequestBody String data) throws SQLException {
		float time = i++;
		try {
			db.pooledConnectionTest(data,time);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return "Saved Data";
	}

	@RequestMapping("/gilevel")
	public String sendgilevel() throws SQLException {
		return db.getGILevel();
	}

	@RequestMapping("/time")
	public String sendtime() throws SQLException {
		return db.getTime();
	}

	@RequestMapping("/insulin")
	public String sendinsulin() throws SQLException {
		return db.getInsulin();
	}

	@PostMapping("/lol")
	public String recieveData(@RequestBody String data) {
		System.out.println("Received data: " + data);
		return "Data received successfully!";
	}

}