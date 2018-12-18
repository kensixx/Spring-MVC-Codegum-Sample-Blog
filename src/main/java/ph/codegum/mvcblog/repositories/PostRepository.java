package ph.codegum.mvcblog.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ph.codegum.mvcblog.models.Post;

import java.util.List;

/**
 * @author Ken on 12/16/2018
 * @project Spring-MVC-Blog
 */

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // This JPQL query will be automatically implemented and mapped to the method findLatest5Posts()
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.author ORDER BY p.date DESC")
    List<Post> findLatest5Posts(Pageable pageable);
}
