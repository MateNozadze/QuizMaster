package ge.tsu.Quiz.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuizForm {
    private String quizName;
    private List<QuestionForm> questions = new ArrayList<>();
}
