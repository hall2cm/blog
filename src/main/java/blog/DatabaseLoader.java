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
    private final UserRepository userRepository;

    @Autowired
    public DatabaseLoader(BlogPostRepository blogPostRepository, UserRepository userRepository) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        User user = new User("Cory Hall", "hall2cm@gmail.com", "https://lh3.googleusercontent.com/-LjjClKrKzsI/AAAAAAAAAAI/AAAAAAAAFno/JQmdSiB-eZg/photo.jpg", "google.com", "103882873928594420713", "Creator");

        this.userRepository.save(user);



        this.blogPostRepository.save(new BlogPost(user,"This is my first blog post", Calendar.getInstance().getTime(), "Blog Post 1", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg"));
        this.blogPostRepository.save(new BlogPost(user, "This is my second blog post", Calendar.getInstance().getTime(), "Blog Post 2", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg"));
        this.blogPostRepository.save(new BlogPost(user, "This is my third blog post", Calendar.getInstance().getTime(), "Blog Post 3", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg"));
        this.blogPostRepository.save(new BlogPost(user,"This is my fourth blog post", Calendar.getInstance().getTime(), "Blog Post 4", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg"));
    }

}
