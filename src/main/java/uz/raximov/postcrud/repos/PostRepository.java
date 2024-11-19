package uz.raximov.postcrud.repos;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.raximov.postcrud.domain.Post;
import uz.raximov.postcrud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Post findFirstByUser(User user);

}
