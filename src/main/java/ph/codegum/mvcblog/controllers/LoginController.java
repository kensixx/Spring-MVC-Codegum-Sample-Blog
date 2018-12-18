package ph.codegum.mvcblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ph.codegum.mvcblog.forms.LoginForm;
import ph.codegum.mvcblog.models.User;
import ph.codegum.mvcblog.services.NotificationService;
import ph.codegum.mvcblog.services.UserService;

import javax.validation.Valid;

/**
 * @author Ken on 12/14/2018
 * @project Spring-MVC-Blog
 */

@Controller
public class LoginController {
    private UserService userService;

    private NotificationService notifyService;

    @Autowired
    // magagamit ung UserServiceStubImpl dito =) dependency inject =)
    public LoginController(UserService userService, NotificationService notifyService) {
        this.userService = userService;
        this.notifyService = notifyService;
    }

    // pag ito yung nilagay sa link
    @RequestMapping("/users/login")
    // tumatanggap ng loginform yan dre
    // ibato mo yung login.html na view
    public String login(LoginForm loginForm) {
        // User doesn't need to re-enter credentials
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken) {
            return "users/login";
        }

        return "redirect:/";
    }

    @RequestMapping("/users/register")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();

        User user = new User();

        modelAndView.addObject(user);

        modelAndView.setViewName("users/register");

        return modelAndView;
    }

    @RequestMapping(value = "users/register", method = RequestMethod.POST)
    public ModelAndView registration(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        User userExists = this.userService.findByUsername(user.getUsername());

        modelAndView.setViewName("users/register");

        if (userExists != null) {
            bindingResult.rejectValue("username", "error.user", "User exists");
        }

        if (!bindingResult.hasErrors()) {
            this.userService.create(user);
            modelAndView.addObject("successMessage", "User has been created");
            modelAndView.addObject("user", new User());
        }

        return modelAndView;
    }
}
