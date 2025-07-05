package ge.tsu.Quiz.config4profiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Profile("dev")//პროფილი რომელზეც არიდ დამატებული ახალი ფუნქცია description
public class DevConfig {
    private static final Logger logger = LoggerFactory.getLogger(DevConfig.class);

    @Bean(name = "devQuizConfig")
    public QuizConfig quizConfig() {
        logger.info(">>> DevConfig profile is activated. Dev log has been printed!");

        QuizConfig config = new QuizConfig();
        return config;
    }
}