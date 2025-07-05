package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.config4profiles.QuizConfig;
import ge.tsu.Quiz.model.Question;
import ge.tsu.Quiz.model.Quiz;
import ge.tsu.Quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class QuizController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    private final QuizService quizService;
    private Quiz currentQuiz;
    @Autowired
    private QuizConfig quizConfig;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/addQuestion")
    public String addQuestion(Model model, @ModelAttribute("questionCount") Integer questionCount,
                              @ModelAttribute("quizName") String quizName,
                              @ModelAttribute("extraOption") String extraOption) {
        logger.debug("GET /addQuestion called with quizName={}, questionCount={}, extraOption={}",
                quizName, questionCount, extraOption);

        if (questionCount == null || questionCount <= 0 || quizName == null || quizName.isBlank()) {
            logger.warn("Invalid quiz setup data: quizName='{}', questionCount={}", quizName, questionCount);

            return "redirect:/setup";
        }
        if (currentQuiz == null) {
            logger.info("Creating new quiz '{}'", quizName);
            currentQuiz = new Quiz();
            currentQuiz.setName(quizName);
            currentQuiz.setQuestionCount(questionCount);
            if (quizConfig.isEnableDescription()) {
                currentQuiz.setDescription(extraOption);
            }
            currentQuiz.setQuestions(new ArrayList<>());
            currentQuiz = quizService.saveQuiz(currentQuiz);
        }
        model.addAttribute("extraOption", quizConfig.isEnableDescription() ? extraOption : null);
        model.addAttribute("question", new Question());
        model.addAttribute("remainingQuestions", questionCount - currentQuiz.getQuestions().size());
        model.addAttribute("enableDescription", quizConfig.isEnableDescription());
        return "addQuestion";
    }

    @PostMapping("/addQuestion")
    public String saveQuestion(@ModelAttribute Question question,
                               @RequestParam("correctAnswersInput") String correctAnswersInput,
                               RedirectAttributes redirectAttributes) {
        logger.debug("POST /addQuestion - Adding question: {}", question.getText());

        if (currentQuiz != null) {
            if (correctAnswersInput != null && !correctAnswersInput.isEmpty()) {
                try {
                    List<Integer> correctAnswers = Arrays.stream(correctAnswersInput.split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    question.setCorrectAnswers(correctAnswers);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid correct answer format: '{}'", correctAnswersInput, e);
                    redirectAttributes.addFlashAttribute("error", "Invalid correct answer format");
                    redirectAttributes.addFlashAttribute("quizName", currentQuiz.getName());
                    redirectAttributes.addFlashAttribute("questionCount", currentQuiz.getQuestionCount());
                    if (quizConfig.isEnableDescription()) {
                        redirectAttributes.addFlashAttribute("extraOption", currentQuiz.getDescription());
                    }
                    return "redirect:/addQuestion";
                }
            } else {
                question.setCorrectAnswers(new ArrayList<>());
            }

            question.setQuiz(currentQuiz);
            currentQuiz.getQuestions().add(question);
            quizService.saveQuiz(currentQuiz);
            logger.debug("Question added. Total questions so far: {}", currentQuiz.getQuestions().size());

            if (currentQuiz.getQuestions().size() < currentQuiz.getQuestionCount()) {
                redirectAttributes.addFlashAttribute("quizName", currentQuiz.getName());
                redirectAttributes.addFlashAttribute("questionCount", currentQuiz.getQuestionCount());
                if (quizConfig.isEnableDescription()) {
                    redirectAttributes.addFlashAttribute("extraOption", currentQuiz.getDescription());
                }
                return "redirect:/addQuestion";
            } else {
                logger.info("All questions added for quiz '{}', redirecting to /quizzes", currentQuiz.getName());

                quizService.saveQuiz(currentQuiz);
                currentQuiz = null;
                return "redirect:/quizzes";
            }
        }
        logger.warn("Attempted to save question but currentQuiz was null");
        return "redirect:/setup";
    }

    @GetMapping("/quizzes")
    public String quizzes(Model model) {
        logger.debug("GET /quizzes - loading all quizzes");

        List<Quiz> quizzes = quizService.getAllQuizzes();
        logger.debug("Loaded {} quizzes", quizzes.size());

        model.addAttribute("quizzes", quizzes);
        model.addAttribute("appName", quizConfig.getAppName() != null ? quizConfig.getAppName() : "DefaultQuizApp");
        model.addAttribute("enableDescription", quizConfig.isEnableDescription());
        return "quizzes";
    }
    @GetMapping("/quiz/start/{quizId}")
    public String startQuiz(@PathVariable Long quizId,
                            @RequestParam(name = "q", defaultValue = "0") int questionIndex,
                            Model model) {
        logger.debug("GET /quiz/start/{}?q={}", quizId, questionIndex);

        Quiz quiz = quizService.getQuizById(quizId);
        if (quiz == null || quiz.getQuestions().isEmpty()) {
            logger.warn("Quiz with id={} not found or has no questions", quizId);

            return "redirect:/quizzes";
        }

        List<Question> questions = quiz.getQuestions();

        if (questionIndex >= questions.size()) {
            logger.warn("Requested questionIndex={} out of bounds for quiz id={}", questionIndex, quizId);

            return "redirect:/quiz/start/" + quizId + "?q=0"; // თავიდან დაიწყე
        }

        Question currentQuestion = questions.get(questionIndex);
        logger.debug("Presenting question {} of quiz '{}'", questionIndex + 1, quiz.getName());

        model.addAttribute("quiz", quiz);
        model.addAttribute("question", currentQuestion);
        model.addAttribute("questionIndex", questionIndex);
        model.addAttribute("isLastQuestion", questionIndex == questions.size() - 1);

        return "quiz-question"; // thymeleaf ფაილი რომელიც ახლა გავაკეთებთ
    }

    @PostMapping("/quiz/submit")
    public String submitQuiz(@RequestParam Long quizId,
                             @RequestParam MultiValueMap<String, String> answers,
                             Model model) {
        logger.debug("POST /quiz/submit - Submitting quiz with ID={}", quizId);

        Quiz quiz = quizService.getQuizById(quizId);
        if (quiz == null) {
            logger.warn("Quiz with id={} not found during submission", quizId);

            return "redirect:/quizzes";
        }
        int correctCount = 0;
        for (Question question : quiz.getQuestions()) {
            String submitted = answers.getFirst("q" + question.getQuestionId());
            if (submitted != null) {
                try {
                    int answerIndex = Integer.parseInt(submitted);
                    if (question.getCorrectAnswers().contains(answerIndex)) {
                        correctCount++;
                    }
                } catch (NumberFormatException ignored) {
                    logger.warn("Invalid submitted answer '{}' for question id={}", submitted, question.getQuestionId());

                }
            }
        }
        logger.info("User submitted quiz '{}': score {}/{}", quiz.getName(), correctCount, quiz.getQuestions().size());

        model.addAttribute("quiz", quiz);
        model.addAttribute("score", correctCount);
        model.addAttribute("total", quiz.getQuestions().size());

        return "quiz-result"; // ამ გვერდზე გამოიტანე შედეგი
    }

}