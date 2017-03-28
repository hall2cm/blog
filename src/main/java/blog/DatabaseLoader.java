package blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cohall on 3/22/2017.
 */

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final BlogPostRepository blogPostRepository;

    @Autowired
    public DatabaseLoader(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        this.blogPostRepository.save(new BlogPost("This is my first blog post", Calendar.getInstance().getTime(), "Cory Hall", "Blog Post 1", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg"));
        this.blogPostRepository.save(new BlogPost("This is my second blog post", Calendar.getInstance().getTime(), "Cory Hall", "Blog Post 2", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg"));
        this.blogPostRepository.save(new BlogPost("This is my third blog post", Calendar.getInstance().getTime(), "Cory Hall", "Blog Post 3", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg"));
        this.blogPostRepository.save(new BlogPost("This is my fourth blog post", Calendar.getInstance().getTime(), "Cory Hall", "Blog Post 4", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg"));
    }

}
