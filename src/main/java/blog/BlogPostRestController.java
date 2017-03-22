package blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Date;

/**
 * Created by cohall on 3/22/2017.
 */

@RestController
@RequestMapping("/posts")
class BlogPostRestController {

    private final BlogPostRepository blogPostRepository;

    @Autowired
    BlogPostRestController(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<BlogPost> readPosts() {
        return this.blogPostRepository.findAllByOrderByPostedDateDesc();
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody BlogPost input) {
        BlogPost result = blogPostRepository.save(new BlogPost(input.postText, new Date(2017-03-22), input.author, input.title));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.GET, value="/{blogPostId}")
    BlogPost readBlogPost(@PathVariable Long blogPostId) {
        return this.blogPostRepository.findOne(blogPostId);
    }

}
