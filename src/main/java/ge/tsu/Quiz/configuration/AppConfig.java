package ge.tsu.Quiz.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:myconfig.yml", factory = ge.tsu.Quiz.configuration.YamlPropertySourceFactory.class)
public class AppConfig {
}
