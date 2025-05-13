package ge.tsu.Quiz.dto;

import lombok.Data;

@Data
public class QuizSetupForm {
    private String quizName;
    private int questionCount;
}
