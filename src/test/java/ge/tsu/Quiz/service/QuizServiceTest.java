package ge.tsu.Quiz.service;

import ge.tsu.Quiz.model.Quiz;
import ge.tsu.Quiz.repository.QuizRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizService quizService;

    private Quiz sampleQuiz;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleQuiz = new Quiz();
        sampleQuiz.setName("Default Quiz");
        sampleQuiz.setDescription("Default Description");
        sampleQuiz.setQuestionCount(3);
        System.out.println("BeforeEach executed");
    }

    @AfterEach
    void tearDown() {
        sampleQuiz = null;
        System.out.println("AfterEach executed");
    }

    @Test
    void testSaveQuiz() {
        when(quizRepository.save(any(Quiz.class))).thenReturn(sampleQuiz);

        Quiz saved = quizService.saveQuiz(sampleQuiz);

        assertNotNull(saved);
        assertEquals("Default Quiz", saved.getName());
        assertTrue(saved.getQuestionCount() > 0);
        assertFalse(saved.getDescription().isEmpty());
        verify(quizRepository).save(sampleQuiz);
    }

    @ParameterizedTest
    @ValueSource(strings = {"History Quiz", "Geography Quiz", "Science Quiz"})
    void testParameterizedQuizNames(String quizName) {
        Quiz quiz = new Quiz();
        quiz.setName(quizName);
        quiz.setDescription("Test Description");
        quiz.setQuestionCount(5);

        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);

        Quiz saved = quizService.saveQuiz(quiz);

        assertEquals(quizName, saved.getName());
        assertNotNull(saved.getDescription());
        assertTrue(saved.getQuestionCount() > 0);
    }

    @Test
    void testGetQuizById_notFound() {
        when(quizRepository.findById(1L)).thenReturn(Optional.empty());

        Quiz result = quizService.getQuizById(1L);

        assertNull(result);
    }
}
