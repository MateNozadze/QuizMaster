package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.config4profiles.QuizConfig;
import ge.tsu.Quiz.dto.QuizSetupForm;
import ge.tsu.Quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class QuizSetupController {
    private static final Logger logger = LoggerFactory.getLogger(QuizSetupController.class);
    private final QuizService quizService;
    private final QuizConfig quizConfig;

    @Autowired
    public QuizSetupController(QuizService quizService, QuizConfig quizConfig) {
        this.quizService = quizService;
        this.quizConfig = quizConfig;
    }

    @GetMapping("/setup")
    public String showSetupForm(Model model) {
        logger.debug("Showing setup form with maxQuestions: {}, enableDescription: {}",
                quizConfig.getMaxQuestions(), quizConfig.isEnableDescription());
        model.addAttribute("quizSetup", new QuizSetupForm());
        model.addAttribute("enableDescription", quizConfig.isEnableDescription());
        model.addAttribute("maxQuestions", quizConfig.getMaxQuestions());
        return "setup";
    }

    @PostMapping("/setup")
    public String handleSetup(@ModelAttribute QuizSetupForm quizSetup,
                              RedirectAttributes redirectAttributes) {
        logger.debug("Handling setup: quizName={}, questionCount={}, extraOption={}",
                quizSetup.getQuizName(), quizSetup.getQuestionCount(), quizSetup.getExtraOption());
        if (quizSetup.getQuestionCount() > quizConfig.getMaxQuestions() || quizSetup.getQuestionCount() < 1) {
            logger.warn("Invalid question count: {}", quizSetup.getQuestionCount());
            redirectAttributes.addFlashAttribute("error", "Invalid question count");
            return "redirect:/setup";
        }
        redirectAttributes.addFlashAttribute("quizName", quizSetup.getQuizName());
        redirectAttributes.addFlashAttribute("questionCount", quizSetup.getQuestionCount());
        if (quizConfig.isEnableDescription()) {
            redirectAttributes.addFlashAttribute("extraOption", quizSetup.getExtraOption());
        }
        return "redirect:/addQuestion";
    }
}