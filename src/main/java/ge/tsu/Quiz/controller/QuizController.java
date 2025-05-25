package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.config4profiles.QuizConfig;
import ge.tsu.Quiz.model.Question;
import ge.tsu.Quiz.model.Quiz;
import ge.tsu.Quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class QuizController {
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
        if (questionCount == null || questionCount <= 0 || quizName == null || quizName.isBlank()) {
            return "redirect:/setup";
        }
        if (currentQuiz == null) {
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
        if (currentQuiz != null) {
            if (correctAnswersInput != null && !correctAnswersInput.isEmpty()) {
                try {
                    List<Integer> correctAnswers = Arrays.stream(correctAnswersInput.split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    question.setCorrectAnswers(correctAnswers);
                } catch (NumberFormatException e) {
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

            if (currentQuiz.getQuestions().size() < currentQuiz.getQuestionCount()) {
                redirectAttributes.addFlashAttribute("quizName", currentQuiz.getName());
                redirectAttributes.addFlashAttribute("questionCount", currentQuiz.getQuestionCount());
                if (quizConfig.isEnableDescription()) {
                    redirectAttributes.addFlashAttribute("extraOption", currentQuiz.getDescription());
                }
                return "redirect:/addQuestion";
            } else {
                quizService.saveQuiz(currentQuiz);
                currentQuiz = null;
                return "redirect:/quizzes";
            }
        }
        return "redirect:/setup";
    }

    @GetMapping("/quizzes")
    public String quizzes(Model model) {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("appName", quizConfig.getAppName() != null ? quizConfig.getAppName() : "DefaultQuizApp");
        model.addAttribute("enableDescription", quizConfig.isEnableDescription());
        return "quizzes";
    }
}