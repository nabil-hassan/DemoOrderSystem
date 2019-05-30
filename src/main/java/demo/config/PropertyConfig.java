package demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:demo/application.properties")
public class PropertyConfig {
	
}
