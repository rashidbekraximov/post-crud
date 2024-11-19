package uz.raximov.postcrud.repos;

import org.springframework.stereotype.Repository;
import uz.raximov.postcrud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);
}
