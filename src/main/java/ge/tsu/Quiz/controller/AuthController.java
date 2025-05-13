package ge.tsu.Quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    //მომხმარებლის სახელი და პაროლი
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password) {
        System.out.println("enter username: " + username + ", password: " + password);
        return "redirect:/home";//ავტორიზაციის შემდეგ გადავა მთავარ გვერდზე
    }
}