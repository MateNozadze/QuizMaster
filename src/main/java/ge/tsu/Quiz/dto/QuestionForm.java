package ge.tsu.Quiz.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionForm {
    private String text;
    private List<String> options = new ArrayList<>();
    private List<Integer> correctAnswers = new ArrayList<>();
}
