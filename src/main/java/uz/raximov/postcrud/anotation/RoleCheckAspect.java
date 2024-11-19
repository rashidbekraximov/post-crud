package uz.raximov.postcrud.anotation;

import jakarta.annotation.Resource;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uz.raximov.postcrud.domain.User;
import uz.raximov.postcrud.enums.Role;
import uz.raximov.postcrud.model.HttpUserDetails;
import uz.raximov.postcrud.repos.UserRepository;

@Aspect
@Component
public class RoleCheckAspect {

    @Resource
    private UserRepository userRepository;

    @Before("@annotation(requiresRole)")
    public void checkRole(RequiresRole requiresRole) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            throw new AccessDeniedException("User details not available");
        }

        // Assuming HttpUserDetails contains a method getId() to fetch user ID
        if (userDetails instanceof HttpUserDetails httpUserDetails) {
            Long userId = httpUserDetails.id;

            if (userId != null) {
                // Fetch the role using your method to get the user by ID
                Role userRole = getById(userId).getRole();

                // Check if the user has the required role
                String[] requiredRoles = requiresRole.value();
                boolean hasPermission = false;

                for (String role : requiredRoles) {
                    if (userRole.name().equalsIgnoreCase(role)) {
                        hasPermission = true;
                        break; // User has the required role, exit the loop
                    }
                }

                // If no required role matched, throw an AccessDeniedException
                if (!hasPermission) {
                    throw new AccessDeniedException("User does not have the required role: " + String.join(", ", requiredRoles));
                }
            } else {
                throw new AccessDeniedException("User ID is not available");
            }
        } else {
            throw new AccessDeniedException("User details do not include required methods");
        }
    }

    // Sample method to fetch user by ID (to be implemented)
    private User getById(Long id) {
        // This could be from a repository or a service
         return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//        throw new UnsupportedOperationException("Not implemented yet");
    }
}
