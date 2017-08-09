package com.pleasantplaces.blog;

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
    private final CommentsRepository commentsRepository;
    private final ReplyRepository replyRepository;

    @Autowired
    public DatabaseLoader(BlogPostRepository blogPostRepository, UserRepository userRepository, CommentsRepository commentsRepository, ReplyRepository replyRepository) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
        this.commentsRepository = commentsRepository;
        this.replyRepository = replyRepository;
    }

    @Override
    public void run(String... strings) throws Exception {


        /*
        User user = new User("Cory Hall", "hall2cm@gmail.com", "https://lh3.googleusercontent.com/-LjjClKrKzsI/AAAAAAAAAAI/AAAAAAAAFno/JQmdSiB-eZg/photo.jpg", "google.com", "mKwAdc5LcVdJ6WUlexACNTbCele2", "Creator");
        BlogPost blogPost = new BlogPost(user,"This is my fourth blog post", Calendar.getInstance().getTime(), "Blog Post 4", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg", "This is the home page text 4");
        Comments comments = new Comments(blogPost, user, "This is a comment 123", Calendar.getInstance().getTime());

        this.userRepository.save(user);

        this.blogPostRepository.save(new BlogPost(user,"This is my first blog post", Calendar.getInstance().getTime(), "Blog Post 1", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg", "This is the home page text 1"));
        this.blogPostRepository.save(new BlogPost(user, "This is my second blog post", Calendar.getInstance().getTime(), "Blog Post 2", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg", "This is the home page text 2"));
        this.blogPostRepository.save(new BlogPost(user, "This is my third blog post", Calendar.getInstance().getTime(), "Blog Post 3", "http://sarahsblog-test-bucket.s3.amazonaws.com/assets/sample1490732596688.jpg", "This is the home page text 3"));
        this.blogPostRepository.save(blogPost);

        this.commentsRepository.save(new Comments(blogPost, user, "This is a comment 456", Calendar.getInstance().getTime()));
        this.commentsRepository.save(new Comments(blogPost, user, "This is a comment 789", Calendar.getInstance().getTime()));
        this.commentsRepository.save(comments);

        this.replyRepository.save(new Reply(comments, user, "This is a reply", Calendar.getInstance().getTime()));
        this.replyRepository.save(new Reply(comments, user, "This is another reply", Calendar.getInstance().getTime()));
        */
         
    }



}
