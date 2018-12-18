package ph.codegum.mvcblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ph.codegum.mvcblog.models.User;

/**
 * @author Ken on 12/16/2018
 * @project Spring-MVC-Blog
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
