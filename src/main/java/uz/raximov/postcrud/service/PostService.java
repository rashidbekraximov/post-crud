package uz.raximov.postcrud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import uz.raximov.postcrud.domain.Post;
import uz.raximov.postcrud.domain.User;
import uz.raximov.postcrud.enums.Role;
import uz.raximov.postcrud.model.PostDTO;
import uz.raximov.postcrud.repos.PostRepository;
import uz.raximov.postcrud.repos.UserRepository;
import uz.raximov.postcrud.util.NotFoundException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final CurrenUserService currenUserService;

    public List<PostDTO> findAll(boolean allHas) {
        User user = currenUserService.getCurrentUserBankId();
        Specification<Post> spec = Specification.where(null);

        if (allHas && user.getRole().name().equals(Role.ROLE_POST_ADMIN.name())){
            spec = spec.and(userIdEquals(user.getId())); // Add the userId filter if needed
        }
        final List<Post> posts = postRepository.findAll(spec,Sort.by("id"));

        return posts.stream()
                .map(post -> mapToDTO(post, new PostDTO()))
                .toList();
    }

    public PostDTO get(final Long id) {
        return postRepository.findById(id)
                .map(post -> mapToDTO(post, new PostDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PostDTO postDTO) {
        final Post post = new Post();
        mapToEntity(postDTO, post);
        return postRepository.save(post).getId();
    }

    public void update(final Long id, final PostDTO postDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(postDTO, post);
        post.setUpdatedDate(LocalDateTime.now());
        postRepository.save(post);
    }

    public void delete(final Long id) {
        postRepository.deleteById(id);
    }

    private PostDTO mapToDTO(final Post post, final PostDTO postDTO) {
        postDTO.setId(post.getId());
        postDTO.setContent(post.getContent());
        postDTO.setTitle(post.getTitle());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
        ZonedDateTime uzbekistanZonedDateTime1 = post.getUpdatedDate().atZone(ZoneId.of("Asia/Tashkent"));
        ZonedDateTime uzbekistanZonedDateTime2 = post.getCreateDate().atZone(ZoneId.of("Asia/Tashkent"));
        postDTO.setUpdatedDate(uzbekistanZonedDateTime1.format(formatter));
        postDTO.setCreateDate(uzbekistanZonedDateTime2.format(formatter));
        postDTO.setUser(post.getUser());
        return postDTO;
    }

    private Post mapToEntity(final PostDTO postDTO, final Post post) {
        post.setContent(postDTO.getContent());
        post.setTitle(postDTO.getTitle());
        final User user = currenUserService.getCurrentUserBankId();
        post.setUser(user);
        post.setUserId(user.getId());
        return post;
    }

    private Specification<Post> userIdEquals(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId);
    }
}
