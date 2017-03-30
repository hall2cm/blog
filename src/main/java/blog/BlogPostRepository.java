package blog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by cohall on 3/21/2017.
 */

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findAllByOrderByPostedDateDesc();

    List<BlogPost> findByUserUid(String Uid);

}
