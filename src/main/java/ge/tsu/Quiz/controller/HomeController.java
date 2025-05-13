package ge.tsu.Quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    //თავიდანვე საიტზე შესვლისას გადაამისამართებს დარეგისტირებაზე
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }
    // ავტორიზაციის შემდეგ გადადის მთავარ გვერდზე (index)
    @GetMapping("/home")
    public String homePage() {
        return "index";
    }
}