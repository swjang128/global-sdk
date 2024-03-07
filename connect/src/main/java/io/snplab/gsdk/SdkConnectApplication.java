package io.snplab.gsdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SdkConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdkConnectApplication.class, args);
	}

}
