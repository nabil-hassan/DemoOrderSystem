package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.verifier.CustomerVerifier;
import demo.verifier.OrderVerifier;

@Configuration
public class TestConfig {

	@Bean
	public CustomerVerifier customerVerifier() {
		return new CustomerVerifier();
	}

	@Bean
	public OrderVerifier orderVerifier() {
		return new OrderVerifier();
	}

}
