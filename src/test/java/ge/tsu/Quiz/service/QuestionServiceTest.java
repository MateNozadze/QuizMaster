package ge.tsu.Quiz.service;

import ge.tsu.Quiz.model.Question;
import ge.tsu.Quiz.repository.QuestionRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question question;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        question = new Question();
        question.setText("What is 2 + 2?");
        System.out.println("BeforeEach executed");
    }

    @AfterEach
    void tearDown() {
        question = null;
        System.out.println("AfterEach executed");
    }

    @Test
    void testSaveQuestion() {
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        Question saved = questionService.saveQuestion(question);

        assertNotNull(saved);
        assertEquals("What is 2 + 2?", saved.getText());
        verify(questionRepository).save(question);
    }

    @Test
    void testGetAllQuestions() {
        when(questionRepository.findAll()).thenReturn(List.of(question));

        List<Question> result = questionService.getAllQuestions();

        assertEquals(1, result.size());
        verify(questionRepository).findAll();
    }

    @Test
    void testGetQuestion_found() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        Optional<Question> result = questionService.getQuestion(1L);

        assertTrue(result.isPresent());
        assertEquals("What is 2 + 2?", result.get().getText());
    }

    @Test
    void testGetQuestion_notFound() {
        when(questionRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Question> result = questionService.getQuestion(2L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteQuestion() {
        doNothing().when(questionRepository).deleteById(1L);

        questionService.deleteQuestion(1L);

        verify(questionRepository).deleteById(1L);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "What is the capital of France?",
            "What is 5 * 5?",
            "Who wrote 'Hamlet'?"
    })
    void testParameterizedQuestions(String questionText) {
        Question q = new Question();
        q.setText(questionText);

        when(questionRepository.save(any(Question.class))).thenReturn(q);

        Question saved = questionService.saveQuestion(q);

        assertEquals(questionText, saved.getText());
        assertNotNull(saved);
        assertTrue(saved.getText().length() > 5);
    }
}
