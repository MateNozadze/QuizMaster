package ge.tsu.Quiz.service;

import ge.tsu.Quiz.model.Quiz;
import ge.tsu.Quiz.repository.QuizRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class QuizService {
    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuiz(Long id) {
        return quizRepository.findById(id);
    }
}
