package ge.tsu.Quiz.repository;

import ge.tsu.Quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
