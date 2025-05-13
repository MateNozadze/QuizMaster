package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.dto.QuizSetupForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class QuizSetupController {
    // GET მოთხოვნა /setup ქვიზის შექმნის ფორმის ჩვენება
    @GetMapping("/setup")
    public String showSetupForm(Model model) {
        model.addAttribute("quizSetup", new QuizSetupForm());
        return "setup";// აბრუნებს setup.html გვერდს
    }

    // POST მოთხოვნა /setup ფორმის მონაცემების დამუშავება
    @PostMapping("/setup")
    public String handleSetup(@ModelAttribute QuizSetupForm quizSetup,
                              RedirectAttributes redirectAttributes) {
        // ვამატებთ ფორმის მონაცემებს დროებით redirect-ისთვის
        redirectAttributes.addFlashAttribute("quizName", quizSetup.getQuizName());
        redirectAttributes.addFlashAttribute("questionCount", quizSetup.getQuestionCount());
        return "redirect:/addQuestion"; // გადამისამართება კითხვების დამატების გვერდზე
    }
}