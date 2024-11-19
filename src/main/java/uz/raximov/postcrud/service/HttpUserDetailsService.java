package uz.raximov.postcrud.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.raximov.postcrud.domain.User;
import uz.raximov.postcrud.enums.Role;
import uz.raximov.postcrud.model.HttpUserDetails;
import uz.raximov.postcrud.repos.UserRepository;

import java.util.Collections;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class HttpUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public HttpUserDetails loadUserByUsername(final String username) {
        final User user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        final List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()));
        return new HttpUserDetails(user.getId(), username, user.getPassword(), authorities);
    }

}
