package com.pleasantplaces.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.URI;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Created by cohall on 3/22/2017.
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
class BlogPostRestController {

    private final BlogPostRepository blogPostRepository;

    private final UserRepository userRepository;

    private final CommentsRepository commentsRepository;

    private final ReplyRepository replyRepository;


    @Autowired
    BlogPostRestController(BlogPostRepository blogPostRepository, UserRepository userRepository, CommentsRepository commentsRepository, ReplyRepository replyRepository) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
        this.commentsRepository = commentsRepository;
        this.replyRepository = replyRepository;
    }

    @Autowired
    ServletContext context;

    @Autowired
    AmazonService amazonService;


    //GET request returns user information and throws error if user does not exist.
    @RequestMapping(method = RequestMethod.GET, value="/user/{userId}")
    User readUser(@PathVariable String userId) {
        this.validateUser(userId);
        return this.userRepository.findByUid(userId).get();
    }

    //GET request to login a user. If user doesn't exist then create a user.
    @RequestMapping(method = RequestMethod.GET, value="/user/login/{userId}")
    Optional<User> loginUser(@PathVariable String userId) {
        return this.userRepository.findByUid(userId);
    }

    //POST request that adds a new user and returns the new location and the user in the body.
    @RequestMapping(method = RequestMethod.POST, value="/user")
    ResponseEntity<?> addUser(@RequestBody User input) {
        User result = userRepository.save(new User(input.displayName, input.email, input.photoUrl, input.providerId, input.uid, "Reader"));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(result);

    }

    //GET request that returns all the posts made by the user and throws an error if the user does not exist.
    @RequestMapping(method = RequestMethod.GET, value="/{userId}/posts")
    List<BlogPost> readUserPosts(@PathVariable String userId) {
        this.validateUser(userId);
        return this.blogPostRepository.findByUserUid(userId);
    }

    //Returns OK for options request.
    @RequestMapping(method = RequestMethod.OPTIONS, value = "/**")
    ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

    //GET request that returns a list of blog posts ordered by posted date.
    @RequestMapping(method = RequestMethod.GET, value="/posts")
    Page<BlogPost> readPosts(Pageable pageable) {
        return this.blogPostRepository.findAllByOrderByPostedDateDesc(pageable);
    }

    //POST request that adds a new blog post and throws an error if the user does not have the access to post.
    @RequestMapping(method = RequestMethod.POST, value="/{userId}/posts")
    ResponseEntity<?> add(@PathVariable String userId, @RequestBody BlogPost input) {
        this.validateUserCredentials(userId);

        return this.userRepository
                .findByUid(userId)
                .map(user -> {
                    BlogPost result = blogPostRepository.save(new BlogPost(user, input.postText, Calendar.getInstance().getTime(), input.title, input.imageLocation, input.homeText));

                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri();


                    return ResponseEntity.created(location).body(result);
                })
                .orElse(ResponseEntity.noContent().build());

    }

    //PUT request that will update a blog post.
    @RequestMapping(method = RequestMethod.PUT, value="/{userId}/posts/{blogPostId}")
    ResponseEntity<?> update(@PathVariable String userId, @PathVariable Long blogPostId, @RequestBody BlogPost input) {
        this.validateUserCredentials(userId);

        BlogPost blogPost = this.blogPostRepository.findOne(blogPostId);
        blogPost.setHomeText(input.homeText);
        blogPost.setImageLocation(input.imageLocation);
        blogPost.setPostText(input.postText);
        blogPost.setTitle(input.title);
        this.blogPostRepository.save(blogPost);

        return ResponseEntity.ok().body(blogPost);

    }

    //POST request that adds a new post comment
    @RequestMapping(method = RequestMethod.POST, value="/posts/{blogPostId}/{userId}/comment")
    ResponseEntity<?> addComment(@PathVariable Long blogPostId, @PathVariable String userId, @RequestBody Comments input) {
        this.validateUser(userId);

        BlogPost blogPost = this.blogPostRepository.findOne(blogPostId);
        String postTitle = blogPost.getTitle();
        String postText = input.commentText;

        User user1 = this.userRepository.findByUid(userId).get();
        String displayName = user1.getDisplayName();
        System.out.println(displayName);

        String subject = "Pleasant Places Blog - Reply to Your Comment";


        AmazonSES amazonSES = new AmazonSES();
        amazonSES.setName(displayName);
        amazonSES.setPostTitle(postTitle);
        amazonSES.setSubject(subject);
        amazonSES.setPostText(postText);
        try {
            amazonSES.readHtmlFile("comment-email.html", blogPostId);
            amazonService.sendEmail();
        } catch (Exception e) {
            System.out.println("Comment Error" + e.getMessage());
        }

        return this.userRepository
                .findByUid(userId)
                .map(user -> {
                    Comments result = commentsRepository.save(new Comments(blogPost, user, input.commentText, Calendar.getInstance().getTime()));

                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri();

                    return ResponseEntity.created(location).body(result);
                })
                .orElse(ResponseEntity.noContent().build());
    }


    //POST request that adds a new post comment
    @RequestMapping(method = RequestMethod.POST, value="/posts/{commentId}/{userId}/reply")
    ResponseEntity<?> addReply(@PathVariable Long commentId, @PathVariable String userId, @RequestBody Reply input) {
        this.validateUser(userId);

        Comments comments = this.commentsRepository.findOne(commentId);
        BlogPost blogPost = comments.getBlogPost();

        String postTitle = blogPost.getTitle();
        String postText = input.replyText;

        User user1 = this.userRepository.findByUid(userId).get();

        User toUser = comments.getUser();
        String displayName = user1.getDisplayName();
        System.out.println(displayName);

        String subject = "Pleasant Places Blog - Reply to Your Comment";


        AmazonSES amazonSES = new AmazonSES();
        amazonSES.setName(displayName);
        amazonSES.setToEmail(toUser.getEmail());
        amazonSES.setPostTitle(postTitle);
        amazonSES.setSubject(subject);
        amazonSES.setPostText(postText);
        try {
            amazonSES.readHtmlFile("reply-email.html", blogPost.getId());
            amazonService.sendEmail();
        } catch (Exception e) {
            System.out.println("Comment Error" + e.getMessage());
        }

        return this.userRepository
                .findByUid(userId)
                .map(user -> {
                    Reply result = replyRepository.save(new Reply(comments, user, input.replyText, Calendar.getInstance().getTime()));

                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri();

                    return ResponseEntity.created(location).body(result);
                })
                .orElse(ResponseEntity.noContent().build());
    }


    //GET request that returns an individual blog post.
    @RequestMapping(method = RequestMethod.GET, value="/posts/{blogPostId}")
    BlogPost readBlogPost(@PathVariable Long blogPostId) {
        return this.blogPostRepository.findOne(blogPostId);
    }

    //POST request that uploads files to S3 and returns the location of the uploaded file.
    @RequestMapping(value="/posts/fileupload", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
    ResponseEntity<FileInfo> upload(@RequestParam("file") MultipartFile inputFile) {
        UploadS3 uploadS3 = new UploadS3();
        FileInfo fileInfo = new FileInfo();
        HttpHeaders headers = new HttpHeaders();
        if(!inputFile.isEmpty()) {
            try {
                String originalFileName = inputFile.getOriginalFilename().split("\\.")[0] + Calendar.getInstance().getTimeInMillis() + "." + inputFile.getOriginalFilename().split("\\.")[1];
                File destinationFile = new File(context.getRealPath("") + File.separator + originalFileName);
                inputFile.transferTo(destinationFile);
                uploadS3.setKeyName(originalFileName);
                uploadS3.setUploadFile(destinationFile);
                uploadS3.setUploadFileName(originalFileName);
                UploadS3.main(null);


                fileInfo.setLocation("/images/" + originalFileName);
                //fileInfo.setFileSize((inputFile.getSize()));
                headers.add("File Uploaded Successfully - ", originalFileName);
                return new ResponseEntity<FileInfo>(fileInfo, headers, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
        }
    }





    //validates that the user exists.
    private void validateUser(String userId) {
        this.userRepository.findByUid(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }

    //validates that the user has the Creator role and is allowed to create blog posts.
    private void validateUserCredentials(String userId) {

        String role = "Creator";

        this.userRepository.findByUidAndRole(userId, role).orElseThrow(
                () -> new UserNotAllowedException(userId));

    }

    private void validateUserLogin(String userId) {
        if (!this.userRepository.findByUid(userId).isPresent()) {

        }
    }

}
