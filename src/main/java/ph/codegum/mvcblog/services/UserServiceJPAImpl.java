package ph.codegum.mvcblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ph.codegum.mvcblog.models.User;
import ph.codegum.mvcblog.repositories.UserRepository;

import java.util.List;

/**
 * @author Ken on 12/16/2018
 * @project Spring-MVC-Blog
 */

@Service
@Primary
public class UserServiceJPAImpl implements UserService {
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceJPAImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.getOne(id);
    }

    @Override
    public User create(User user) {
        // Encore user's password before adding it to database
        user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPasswordHash()));
        return this.userRepository.save(user);
    }

    @Override
    public User edit(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
}
