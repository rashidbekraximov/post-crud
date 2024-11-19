package uz.raximov.postcrud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.raximov.postcrud.service.PostService;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/home")
    public String index(Model model) {
        model.addAttribute("posts", postService.findAll(false));
        return "home/index";
    }

}
