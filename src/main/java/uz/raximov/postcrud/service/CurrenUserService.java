package uz.raximov.postcrud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.raximov.postcrud.domain.User;
import uz.raximov.postcrud.repos.UserRepository;

@Service
@RequiredArgsConstructor
public class CurrenUserService {

    private final UserRepository userRepository;

    public User getCurrentUserBankId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            // Fetch the user from the database
            return userRepository.findByUsernameIgnoreCase(username); // Return the bankId of the found user
        }
        return null;
    }
}
