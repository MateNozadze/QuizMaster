package ge.tsu.Quiz.config4profiles;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "quiz.app")
public class QuizConfig {

    private String appName;
    @Min(1)
    private int maxQuestions;
    private boolean featureEnabled;
    private boolean debugMode =false;



    public boolean isEnableDescription() { return featureEnabled; }
    public void setEnableDescription(boolean featureEnabled) { this.featureEnabled = featureEnabled; }

}