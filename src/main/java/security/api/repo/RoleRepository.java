package security.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import security.api.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
