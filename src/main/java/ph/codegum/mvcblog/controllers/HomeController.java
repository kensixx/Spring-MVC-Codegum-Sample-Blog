package ph.codegum.mvcblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ph.codegum.mvcblog.models.Post;
import ph.codegum.mvcblog.services.PostService;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 *
 * @author Ken on 12/12/2018
 * @project Spring-MVC-Blog
 */

@Controller // define a Spring Web MVC Controller
public class HomeController {

    @Autowired
    // this is actually the stub implemenatation (PostServiceStubImpl)
    // ito yung ginamit as bean dahil sa @Service annotation
    // kaya pag kinuha dito sa ibang controller, ang makukuha actually is PostServiceStubImpl
    // kasi nakainterface sya sa PostService.
    // ito yata yung sinasabi nilang "the object doesn't know what type of PostService it should run"
    // pero ok lng un. sana malaman ko pa ng mas in-depth.
    private PostService postService;

    // when browser GET requests "/"
    @RequestMapping(value = {"/", "/home"})
    // run this index() method
    public String index(Model model) { // Model model is the view model
        List<Post> latest5Posts = postService.findLatest5(); // this is actualy from the stub impl
        // para magamit sa front end. name nya is "latest5posts"
        model.addAttribute("latest5posts", latest5Posts);

        List<Post> latest3Posts = latest5Posts.stream().limit(3).collect(Collectors.toList());
        model.addAttribute("latest3posts", latest3Posts);

        // using Thymeleaf, this will render
        // the "index.html" template.
        // kung ano ung model na in-addAttribute sa controller
        // v yun yung magagamit dito
        return "index";
    }
}
