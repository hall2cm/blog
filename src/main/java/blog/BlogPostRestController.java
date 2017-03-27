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

/**
 * Created by cohall on 3/22/2017.
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/posts")
class BlogPostRestController {

    private final BlogPostRepository blogPostRepository;

    @Autowired
    BlogPostRestController(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Autowired
    ServletContext context;

    @RequestMapping(method = RequestMethod.OPTIONS)
    ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    List<BlogPost> readPosts() {
        return this.blogPostRepository.findAllByOrderByPostedDateDesc();
    }


    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody BlogPost input) {
        BlogPost result = blogPostRepository.save(new BlogPost(input.postText, Calendar.getInstance().getTime(), input.author, input.title));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(result);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{blogPostId}")
    BlogPost readBlogPost(@PathVariable Long blogPostId) {
        return this.blogPostRepository.findOne(blogPostId);
    }

    @RequestMapping(value="/fileupload", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
    ResponseEntity<FileInfo> upload(@RequestParam("file") MultipartFile inputFile) {
        FileInfo fileInfo = new FileInfo();
        HttpHeaders headers = new HttpHeaders();
        if(!inputFile.isEmpty()) {
            try {
                String originalFileName = inputFile.getOriginalFilename();
                File destinationFile = new File("C:\\Users\\cohall\\angular-blog\\src"+ File.separator + originalFileName);
                inputFile.transferTo(destinationFile);
                fileInfo.setFileName(destinationFile.getPath());
                fileInfo.setFileSize((inputFile.getSize()));
                headers.add("File Uploaded Successfully - ", originalFileName);
                return new ResponseEntity<FileInfo>(fileInfo, headers, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
        }
    }

}
