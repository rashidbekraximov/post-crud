package uz.raximov.postcrud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.raximov.postcrud.domain.Post;
import uz.raximov.postcrud.domain.User;
import uz.raximov.postcrud.enums.Role;
import uz.raximov.postcrud.model.UserDTO;
import uz.raximov.postcrud.repos.PostRepository;
import uz.raximov.postcrud.repos.UserRepository;
import uz.raximov.postcrud.util.NotFoundException;
import uz.raximov.postcrud.util.ReferencedWarning;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole().name());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
        ZonedDateTime uzbekistanZonedDateTime = user.getCreateDate().atZone(ZoneId.of("Asia/Tashkent"));
        userDTO.setCreateDate(uzbekistanZonedDateTime.format(formatter));
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setRole(Role.valueOf(userDTO.getRole()));
        user.setPassword(userDTO.getPassword() == null ? null : passwordEncoder.encode(userDTO.getPassword()));
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Post postPost = postRepository.findFirstByUser(user);
        if (postPost != null) {
            referencedWarning.setKey("user.post.post.referenced");
            referencedWarning.addParam(postPost.getId());
            return referencedWarning;
        }
        return null;
    }


}
