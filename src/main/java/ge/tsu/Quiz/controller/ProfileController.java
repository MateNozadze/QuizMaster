package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.model.User;
import ge.tsu.Quiz.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);


    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Profile page requested by user '{}'", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        logger.debug("User '{}' found, quizzes count: {}", username, user.getQuizzes().size());

        model.addAttribute("user", user);
        model.addAttribute("quizzes", user.getQuizzes());
        return "profile";
    }
}