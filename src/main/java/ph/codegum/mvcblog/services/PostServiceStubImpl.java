package ph.codegum.mvcblog.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ph.codegum.mvcblog.models.Post;
import ph.codegum.mvcblog.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Stub: sample data, stored in the memory
 *
 * Create stub implementation for PostService interface
 *
 * It will hold the posts in a List<Post> collection and the service methods will be easy to be implemented:
 *
 * @author Ken on 12/13/2018
 * @project Spring-MVC-Blog
 */

// tells Spring that this is a Service and will automatically instantiate and inject it in the controllers
// (through the @Autowired annotation).
@Service
public class PostServiceStubImpl implements PostService {
    private List<Post> posts = new ArrayList<Post>() {{
       add(new Post(1L, "First Post", "<p>Line #1.</p><p>Line #2</p>", null));
       add(new Post(2L, "Second Post", "Second post content:<ul><li>line 1</li><li>line 2</li></p>", new User(10L,"pesho10", "Peter Ivanov")));
       add(new Post(3L, "Post #3", "<p>The post number 3 nice</p>", new User(10L, "merry", null)));
       add(new Post(4L, "Fourth Post", "<p>Not interesting post</p>", new User(11L, "kensixx", null)));
       add(new Post(5L, "Post Number 5", "<p>Just posting</p>", null));
       add(new Post(6L, "Sixth Post", "<p>Another interesting post</p>", null));
    }};

    @Override
    public Page<Post> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Post> findAll() {
        return this.posts;
    }

    @Override
    public List<Post> findLatest5() {
        return this.posts.stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public Post findById(Long id) {
        return this.posts.stream()
                // search from posts where id of object = id
                .filter(p -> Objects.equals(p.getId(), id))
                // get the find result
                .findFirst()
                // else, return null
                .orElse(null);
    }

    @Override
    public Post create(Post post) {
        post.setId(this.posts.stream().mapToLong(p -> p.getId()).max().getAsLong() + 1);

        this.posts.add(post);

        return post;
    }

    @Override
    public Post edit(Post post) {
        // iterate thru all posts inside "posts" List (acting database)
        for (int i = 0; i < this.posts.size(); i++) {

            // if post inside argument has a matching id inside the List
            if (Objects.equals(this.posts.get(i).getId(), post.getId())) {
                // set the post of that post index to the post in the argument
                this.posts.set(i, post);

                return post;
            }
        }

        // pag walang tumama na post at nakarating pa dito sa labas
        // throw this
        throw new RuntimeException("Post not found: " + post.getId());
    }

    @Override
    public void deleteById(Long id) {
        for (int i = 0; i < this.posts.size(); i++) {
            if (Objects.equals(this.posts.get(i).getId(), id)) {
                this.posts.remove(i);
                return;
            }
        }

        throw new RuntimeException("Post not found: " + id);
    }
}
