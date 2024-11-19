package uz.raximov.postcrud.controller;

import uz.raximov.postcrud.domain.User;
import uz.raximov.postcrud.model.PostDTO;
import uz.raximov.postcrud.repos.UserRepository;
import uz.raximov.postcrud.service.PostService;
import uz.raximov.postcrud.util.CustomCollectors;
import uz.raximov.postcrud.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    public PostController(final PostService postService, final UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("postValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("posts", postService.findAll(true));
        return "post/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("post") final PostDTO postDTO) {
        return "post/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("post") final PostDTO postDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors() || postDTO.getContent() == null || postDTO.getContent().isEmpty()) {
            return "post/add";
        }
        postService.create(postDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("post.create.success"));
        return "redirect:/posts";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("post", postService.get(id));
        return "post/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("post") final PostDTO postDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/edit";
        }
        postService.update(id, postDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("post.update.success"));
        return "redirect:/posts";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        postService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("post.delete.success"));
        return "redirect:/posts";
    }

}
