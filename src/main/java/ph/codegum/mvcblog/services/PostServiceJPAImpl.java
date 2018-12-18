package ph.codegum.mvcblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ph.codegum.mvcblog.models.Post;
import ph.codegum.mvcblog.repositories.PostRepository;

import java.util.List;

/**
 * @author Ken on 12/16/2018
 * @project Spring-MVC-Blog
 */

@Service
@Primary // This will tell the Spring Framework to use these implementations instead of the old stubs.
public class PostServiceJPAImpl implements PostService {
    private PostRepository postRepository;

    @Autowired
    public PostServiceJPAImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return this.postRepository.findAll(pageable);
    }

    @Override
    public List<Post> findAll() {
        return this.postRepository.findAll();
    }

    @Override
    public List<Post> findLatest5() {
        return this.postRepository.findLatest5Posts(new PageRequest(0, 5));
    }

    @Override
    public Post findById(Long id) {
        return this.postRepository.findById(id).get();
    }

    @Override
    public Post create(Post post) {
        return this.postRepository.save(post);
    }

    @Override
    public Post edit(Post post) {
        return this.postRepository.save(post);
    }

    @Override
    public void deleteById(Long id) {
        this.postRepository.deleteById(id);
    }
}
