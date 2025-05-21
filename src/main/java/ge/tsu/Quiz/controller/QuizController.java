package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.model.Question;
import ge.tsu.Quiz.model.Quiz;
import ge.tsu.Quiz.service.QuizService;
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

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // GET მოთხოვნა /addQuestion - დამატების გვერდის ჩვენება
    @GetMapping("/addQuestion")
    public String addQuestion(Model model, @ModelAttribute("questionCount") Integer questionCount,
                              @ModelAttribute("quizName") String quizName) {
        // თუ მონაცემები არ არის ვალიდური, ვაბრუნებთ setup გვერდზე
        if (questionCount == null || questionCount <= 0 || quizName == null || quizName.isBlank()) {
            return "redirect:/setup";
        }
        // თუ currentQuiz ჯერ არ არსებობს, ვქმნით და ვინახავთ
        if (currentQuiz == null) {
            currentQuiz = new Quiz();
            currentQuiz.setName(quizName);
            currentQuiz.setQuestionCount(questionCount);
            currentQuiz.setQuestions(new ArrayList<>());
            currentQuiz = quizService.saveQuiz(currentQuiz); // ვინახავთ ბაზაში
            System.out.println("currentQuiz ID after save: " + (currentQuiz != null ? currentQuiz.getId() : "null"));
        }
        // ახალ კითხვას ვამატებთ მოდელში და ვთვლით რამდენი დარჩა
        model.addAttribute("question", new Question());
        model.addAttribute("remainingQuestions", questionCount - currentQuiz.getQuestions().size());
        return "addQuestion";
    }
    //კითხვების შენახვა
    @PostMapping("/addQuestion")
    public String saveQuestion(@ModelAttribute Question question,
                               @RequestParam("correctAnswersInput") String correctAnswersInput,
                               RedirectAttributes redirectAttributes) {
        if (currentQuiz != null) {
            // გარდაქმენი correctAnswersInput -> List<Integer>
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
                    return "redirect:/addQuestion";
                }
            } else {
                question.setCorrectAnswers(new ArrayList<>()); // თუ ცარიელია, ცარიელი სია
            }

            question.setQuiz(currentQuiz); // ვაკავშირებთ მიმდინარე ქვიზს
            currentQuiz.getQuestions().add(question); // ვამატებთ სიაში

            // თუ ჯერ კიდევ დარჩენილია დასამატებელი კითხვები, ვაბრუნებთ უკან
            if (currentQuiz.getQuestions().size() < currentQuiz.getQuestionCount()) {
                redirectAttributes.addFlashAttribute("quizName", currentQuiz.getName());
                redirectAttributes.addFlashAttribute("questionCount", currentQuiz.getQuestionCount());
                return "redirect:/addQuestion";
            } else {
                // ყველა კითხვის დამატების შემდეგ
                for (Question q : currentQuiz.getQuestions()) {
                    q.setQuiz(currentQuiz);
                }
                quizService.saveQuiz(currentQuiz);
                currentQuiz = null;
                return "redirect:/quizzes";
            }
        }
        return "redirect:/setup";
    }
    // ყველა ქვიზის ჩვენება
    @GetMapping("/quizzes")
    public String quizzes(Model model) {
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        return "quizzes";
    }
}
