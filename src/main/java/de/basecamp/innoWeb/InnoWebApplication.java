package de.basecamp.innoWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InnoWebApplication {
	/**
	 * Starts the application. Used for debugging and starting on localhost(standard port is 8080)
 	 * @param args args
	 */
	public static void main(String[] args) { SpringApplication.run(InnoWebApplication.class, args);	}

}
