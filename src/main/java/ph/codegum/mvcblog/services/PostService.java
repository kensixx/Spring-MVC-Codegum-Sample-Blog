package ph.codegum.mvcblog.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ph.codegum.mvcblog.models.Post;

import java.util.List;

/**
 * Post Service Interface that will provide the BUSINESS LOGIC for working with posts in the blog system.
 *
 * Services hold the business logic. Often just call some repository method.
 *
 * ex. create new post / delete post
 *
 * Services may have several implementations: DB based or stub based.
 *
 * @author Ken on 12/13/2018
 * @project Spring-MVC-Blog
 */
public interface PostService {
    Page<Post> findAll(Pageable pageable);

    List<Post> findAll();
    List<Post> findLatest5();


    Post findById(Long id);
    Post create(Post post);
    Post edit(Post post);

    void deleteById(Long id);
}
