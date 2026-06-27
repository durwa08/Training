package restaurantportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantPortalApplication {

	public static void main(String[] args) {

		/**
		 * main method that starts the Spring Boot application.
		 */

		SpringApplication.run(RestaurantPortalApplication.class, args);
		System.out.println("Restaurant Portal App");

	}

}
