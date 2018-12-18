package ph.codegum.mvcblog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ph.codegum.mvcblog.models.User;
import ph.codegum.mvcblog.services.UserService;

import java.util.List;

/**
 * @author Ken on 12/18/2018
 * @project Spring-MVC-Blog
 */

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String index(Model model) {
        List<User> userList =  this.userService.findAll();

        model.addAttribute("userList", userList);

        return "users/index";
    }
}
