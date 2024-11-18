package com.example.chating2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Chating2Application {

	public static void main(String[] args) {
		SpringApplication.run(Chating2Application.class, args);
	}

	@Value("${react.url}")
	private String reactUrl;

	@Bean    //react 주소 //:3000 허용
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")	// '/*' 모든 주소
						.allowedMethods("*")	//GET,POST..
						.allowedOrigins(reactUrl)
				 		.allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}
}
