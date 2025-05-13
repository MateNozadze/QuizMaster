package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.model.Question;
import ge.tsu.Quiz.model.Quiz;
import ge.tsu.Quiz.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

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
    public String saveQuestion(@ModelAttribute Question question, RedirectAttributes redirectAttributes) {
        if (currentQuiz != null) {
            question.setQuiz(currentQuiz);//ვაკავშირებს მიმდინარე ქვიზს
            currentQuiz.getQuestions().add(question);//ვამატებთ სიაში
            // თუ ჯერ კიდევ დარჩენილია დასამატებელი კითხვები, ვაბრუნებთ უკან დამატების გვერდზე
            if (currentQuiz.getQuestions().size() < currentQuiz.getQuestionCount()) {
                redirectAttributes.addFlashAttribute("quizName", currentQuiz.getName());
                redirectAttributes.addFlashAttribute("questionCount", currentQuiz.getQuestionCount());
                return "redirect:/addQuestion";
            } else {
                //ყველა კითხვის დამატების შემდეგ  გადავდივართ ქვიზების გვერდზე
                for (Question q : currentQuiz.getQuestions()) {
                    q.setQuiz(currentQuiz);
                }
                quizService.saveQuiz(currentQuiz);
                currentQuiz = null;
                return "redirect:/quizzes";
            }
        }
        return "redirect:/setup";// უკან დაბრუნება
    }
    // ყველა ქვიზის ჩვენება
    @GetMapping("/quizzes")
    public String quizzes(Model model) {
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        return "quizzes";
    }
}
