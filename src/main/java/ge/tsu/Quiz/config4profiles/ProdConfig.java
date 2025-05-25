package ge.tsu.Quiz.config4profiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")//ამ პროფილზე ჯერ არ არის ჩაშვებული description და მხოლოდ 20 კითხვის დამატება გვაქვვს
public class ProdConfig {

    @Bean(name = "prodQuizConfig")
    public QuizConfig quizConfig() {
        QuizConfig config = new QuizConfig();
        return config;
    }
}