package uz.raximov.postcrud.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.raximov.postcrud.model.AuthenticationRequest;


@Controller
public class AuthenticationController {

    @GetMapping("/")
    public String login(final Model model) {
        // dummy for using the inputRow fragment
        model.addAttribute("authentication", new AuthenticationRequest());
        return "authentication/login";
    }

    // Method to handle form submission
    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) Boolean error,
                        @RequestParam(name = "logoutSuccess", required = false) Boolean logoutSuccess,
                        Model model) {
        if (error != null && error) {
            model.addAttribute("msgError", "Invalid username or password.");
        }
        if (logoutSuccess != null && logoutSuccess) {
            model.addAttribute("msgInfo", "You have been logged out.");
        }
        return "authentication/login";
    }


//    @PostMapping("/login")
//    public String performLogin(@ModelAttribute("authenticationRequest") final AuthenticationRequest authenticationRequest, Model model) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
//            );
//
//            if (authentication.isAuthenticated()) {
//                return "redirect:/home"; // Muvaffaqiyatli login
//            }
//        } catch (AuthenticationException ex) {
//            model.addAttribute("loginError", true);
//            model.addAttribute("errorMessage", "Login yoki parol noto'g'ri.");
//        }
//
//        return "login";
//    }

}
