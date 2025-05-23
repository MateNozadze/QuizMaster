package ge.tsu.Quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    //თავიდანვე საიტზე შესვლისას გადაამისამართებს დარეგისტირებაზე
    @GetMapping("/")
    public String redirectToLoginOrHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // თუ მომხმარებელი ავტორიზებულია (არ არის ანონიმური)
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/home";
        }
        return "redirect:/login";
    }


    // ავტორიზაციის შემდეგ გადადის მთავარ გვერდზე (index)
    @GetMapping("/home")
    public String homePage() {
        return "index";
    }
}