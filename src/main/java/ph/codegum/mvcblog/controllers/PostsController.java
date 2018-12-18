package ph.codegum.mvcblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ph.codegum.mvcblog.models.Post;
import ph.codegum.mvcblog.models.User;
import ph.codegum.mvcblog.services.NotificationService;
import ph.codegum.mvcblog.services.PostService;
import ph.codegum.mvcblog.services.UserService;

import javax.validation.Valid;

/**
 * @author Ken on 12/13/2018
 * @project Spring-MVC-Blog
 */

// register sa Spring container as a Controller Bean.
// this means pwede na sya i-autowire sa ibang class if needed.

// TODO: paano mag DI pag controllers, ano hitsura
@Controller
public class PostsController {

    // 1. I-constructor based DI mo yung kailangan mo,
    // in this case yung postService. (UNG INTERFACE HA).
    private PostService postService;

    // inject NotificationService pero pag ginamit ung .addErrorMessage()
    // iniinject mo talaga ung NotificationServiceImpl.
    // pero ala ka na pake dun basta gmawa ka ng interface
    // alam na ni Spring kung anong Service implementation na may class ung gagamitin mo.
    private NotificationService notifyService;

    // kailangan to kasi may kinalaman na si user dito. sa pag CRUD ng post
    // kinukuha na si user. kailangan tong servicep ara kunin sa repo -> DB.
    private UserService userService;

    @Autowired
    public PostsController (PostService postService, NotificationService notifyService, UserService userService) {
        this.postService = postService;
        this.notifyService = notifyService;
        this.userService = userService;
    }

    // view ALL posts
    @GetMapping("/posts")
    public String index(Model model, @PageableDefault(sort = {"id"}, value = 5) Pageable pageable) {

        // Page is in the PostService.
        // pageable is nasa argument. bale 5 pages (?) iyan.
        Page<Post> posts = this.postService.findAll(pageable);

        // Define variables to be passed to view
        model.addAttribute("posts", posts);

        // Return the view model itself
        return "posts/index";
    }

    // kapag ito yung URL,
    @GetMapping("/posts/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        // 2. kung sinuman ung may .findById() na method sa PostService
        // in this case si StubImpl meron nun.
        // un ung tatawagin ni Spring. Si Spring na bahala dun.
        Post post = postService.findById(id);

        if (post == null) {
            notifyService.addErrorMessage("Cannot find post #" + id + ".");

            // redirect to root?
            return "redirect:/";
        }

        model.addAttribute("post", post);

        // render the view
        // which corresponds to the file “view.html”
        // in directory src/main/resources/templates/posts.
        return "posts/view";
    }

    /*
     * READ create post page
     * Display form to create a post
     */
    @RequestMapping("/posts/create")
    public ModelAndView create() {
        // initialize ModelAndView object
        ModelAndView modelAndView = new ModelAndView();

        // initialize new Post object
        Post post = new Post();

        // add to ModelAndView as a new object
        modelAndView.addObject(post);

        // ??
        modelAndView.setViewName("posts/create");
        return modelAndView;
    }

    @PostMapping(value = "/posts/create")
    public ModelAndView create(@Valid Post post, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("posts/create");

        // Perform validation
        if ( post.getTitle().isEmpty())  {
            bindingResult.rejectValue("title", "error.post", "Title cannot be empty.");
        }

        if ( post.getBody().isEmpty() ) {
            bindingResult.rejectValue("body", "error.post", "Content cannot be empty.");
        }

        // Get author using Spring Security built-in authentication getter
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // using username of current logged in User, it can be retrieved.
        User user = this.userService.findByUsername(auth.getName());

        // More validation AFTER user has been get.

        // Check if user is null.
        if ( user == null ) {
            bindingResult.rejectValue("author","error.post", "Author cannot be null." );
        }

        // if okie na lahat,
        if ( !bindingResult.hasErrors() ) {
            post.setAuthor(user);
            this.postService.create(post);

            // can be used in view to present a successMessage object that contains "post has been created" message
            modelAndView.addObject("successMessage", "Post has been created!");

            // bakit bago?
            modelAndView.addObject("post", new Post());
        }

        return modelAndView;
    }

    /*
     * Display form to edit a post
     * @param id
     * @param model
     * @return
     */

    // get the id from the URL
    @GetMapping("/posts/edit/{id}")
    // @PathVariable annotation means dun isasaksak sa given argument ung galing sa URL mapping
    public String edit(@PathVariable("id") Long id, Model model) {
        // find the post first using the path variable id used in the URL
        Post post = this.postService.findById(id);

        // is yung ni-findById is null, wala na yun pre. balik ka sa view all posts.
        if ( post == null ) {
            notifyService.addErrorMessage("Cannot find post #" + id + ".");
            return "redirect:/posts/";
        }

        // yung na-find by ID, i-add as attribute para magamit sa view.
        model.addAttribute("post", post);

        // dito tayo magusap sa posts/edit.html
        return "posts/edit";
    }

    /**
     * Proceeds to update a post.
     * Receives a Post as an argument.
     * Then dapat i-update nya yung post na object ah
     *
     * Yung Post object na yun ibibigay yun nung view pre.
     *
     * @param post
     * @param bindingResult
     * @return
     */
    @PostMapping("/posts/edit")
    public ModelAndView editPost(@Valid Post post, BindingResult bindingResult) {
        // initialize new ModelAndView (ano ba to?)
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("posts/edit");

        // Perform validation
        if ( post.getTitle().isEmpty() ) {
            bindingResult.rejectValue("title", "error.post", "Title cannot be empty.");
        }

        if ( post.getBody().isEmpty() ) {
            bindingResult.rejectValue("body", "error.post", "Content cannot be empty." );
        }

        // Get the author of the post by ID.
        // Locate in database using the UserService.
        User user = this.userService.findById( post.getAuthor().getId() );

        // Check if user is null, throw an error
        if ( user == null ) {
            bindingResult.rejectValue("author", "error.post", "Author cannot be null.");
        }

        // if no errors dre
        if ( !bindingResult.hasErrors() ) {
            post.setAuthor(user);

            // Note: .create() and .edit() ay iisa lang (sa Spring).
            // Chinecheck na nya kung nageexist yung id.
            // Pag oo, ioverwrite nya yung current id dun sa laman ng POST.
            // Pag hindi, gawa sya bago.
            // Astig.

            // Note 2: .save() ito sa JpaRepository specification.
            this.postService.edit(post);

            modelAndView.addObject("successMessage", "Post has been updated.");
            modelAndView.addObject("post", post);
        }

        return modelAndView;
    }

    @GetMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        Post post = this.postService.findById(id);

        if ( post == null ) {
            notifyService.addErrorMessage("Cannot find post #" + id + ".");
        } else {
            this.postService.deleteById(id);
        }

        return "redirect:/posts/";
    }
}
