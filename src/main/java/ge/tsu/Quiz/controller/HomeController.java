package ge.tsu.Quiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    //თავიდანვე საიტზე შესვლისას გადაამისამართებს დარეგისტირებაზე
    @GetMapping("/")
    public String redirectToLoginOrHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // თუ მომხმარებელი ავტორიზებულია (არ არის ანონიმური)
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            logger.info("Authenticated user '{}' accessed root URL, redirecting to /home", authentication.getName());

            return "redirect:/home";
        }

        logger.info("Anonymous user accessed root URL, redirecting to /login");

        return "redirect:/login";
    }


    // ავტორიზაციის შემდეგ გადადის მთავარ გვერდზე (index)
    @GetMapping("/home")
    public String homePage() {
        logger.info("User accessed /home page");
        return "index";
    }
}