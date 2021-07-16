package com.xenosnowfox.addressbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	/**
	 * Application entrypoint.
	 *
	 * @param withArgs
	 * 		Command line arguments.
	 */
	public static void main(final String[] withArgs) {
		SpringApplication.run(Application.class, withArgs);
	}
}
