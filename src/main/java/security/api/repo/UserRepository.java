package security.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import security.api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
