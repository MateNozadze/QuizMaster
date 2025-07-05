package ge.tsu.Quiz.config4profiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Profile("prod")//ამ პროფილზე ჯერ არ არის ჩაშვებული description და მხოლოდ 20 კითხვის დამატება გვაქვვს
public class ProdConfig {
    private static final Logger logger = LoggerFactory.getLogger(ProdConfig.class);

    @Bean(name = "prodQuizConfig")
    public QuizConfig quizConfig() {
        logger.info(">>> ProdConfig profile is activated. Prod log has been printed!");

        QuizConfig config = new QuizConfig();
        return config;
    }
}