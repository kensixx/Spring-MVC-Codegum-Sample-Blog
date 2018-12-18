package ph.codegum.mvcblog.services;

import ph.codegum.mvcblog.models.User;

import java.util.List;

/**
 * @author Ken on 12/14/2018
 * @project Spring-MVC-Blog
 */
public interface UserService {
    List<User> findAll();

    User findByUsername(String username);
    User findById(Long id);
    User create(User user);
    User edit(User user);

    void deleteById(Long id);
}
