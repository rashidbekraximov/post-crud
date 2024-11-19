package uz.raximov.postcrud.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Provide attributes available in all templates.
 */
@ControllerAdvice
public class WebAdvice {

    @ModelAttribute("requestUri")
    public String getRequestUri(final HttpServletRequest request) {
        return request.getRequestURI();
    }


    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, "Rad etildi: Sizda kerakli ruxsat yo'q.");
            return "redirect:/home";
        }else{
            return "redirect:/";
        }
    }
}
