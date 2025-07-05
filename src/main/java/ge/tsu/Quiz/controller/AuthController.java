package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.model.User;
import ge.tsu.Quiz.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        logger.debug("Login page requested");
        return "login";
    }

    //მომხმარებლის სახელი და პაროლი

    @GetMapping("/signup")
    public String showSignupForm() {
        logger.debug("Signup page requested");
        return "signup";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        logger.info("Login attempt for username: {}", username);
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            logger.info("User '{}' logged in successfully", username);
            return "redirect:/home";
        }
        logger.warn("Failed login attempt for username: {}", username);
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }
    @PostMapping("/signup")
    public String processSignup(@RequestParam String username, @RequestParam String password, Model model) {
        logger.info("Signup attempt for username: {}", username);
        if (userRepository.findByUsername(username).isPresent()) {
            logger.warn("Signup failed: Username '{}' already exists", username);
            model.addAttribute("error", "Username already exists");
            return "signup";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        logger.info("User '{}' registered successfully", username);
        return "redirect:/login";
    }
}