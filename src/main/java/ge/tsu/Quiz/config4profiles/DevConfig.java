package ge.tsu.Quiz.config4profiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")//პროფილი რომელზეც არიდ დამატებული ახალი ფუნქცია description
public class DevConfig {

    @Bean(name = "devQuizConfig")
    public QuizConfig quizConfig() {
        QuizConfig config = new QuizConfig();
        return config;
    }
}