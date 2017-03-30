package blog;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    BlogPostRestController(BlogPostRepository blogPostRepository, UserRepository userRepository) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
    }

    @Autowired
    ServletContext context;


    //GET request returns user information and throws error if user does not exist.
    @RequestMapping(method = RequestMethod.GET, value="/{userId}")
    Optional<User> readUser(@PathVariable String userId) {
        this.validateUser(userId);
        return this.userRepository.findByUid(userId);
    }

    //POST request that adds a new user and returns the new location and the user in the body.
    @RequestMapping(method = RequestMethod.POST, value="/addUser")
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
    List<BlogPost> readPosts() {
        return this.blogPostRepository.findAllByOrderByPostedDateDesc();
    }

    //POST request that adds a new blog post and throws an error if the user does not have the access to post.
    @RequestMapping(method = RequestMethod.POST, value="/{userId}/posts")
    ResponseEntity<?> add(@PathVariable String userId, @RequestBody BlogPost input) {
        this.validateUserCredentials(userId);

        return this.userRepository
                .findByUid(userId)
                .map(user -> {
                    BlogPost result = blogPostRepository.save(new BlogPost(user, input.postText, Calendar.getInstance().getTime(), input.title, input.imageLocation));

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

                fileInfo.setLocation("http://sarahsblog-test-bucket.s3.amazonaws.com/assets/" + originalFileName);
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

}
