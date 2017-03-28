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
        BlogPost result = blogPostRepository.save(new BlogPost(input.postText, Calendar.getInstance().getTime(), input.author, input.title, input.imageLocation));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(result);
    }

/*
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addForm(@RequestParam("imageLocation") MultipartFile inputFile, @RequestParam("author") String author,
                              @RequestParam("title") String title, @RequestParam("postText") String postText) {
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

                BlogPost result = blogPostRepository.save(new BlogPost(postText, Calendar.getInstance().getTime(), author, title, fileInfo.getLocation()));

                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest().path("/{id}")
                        .buildAndExpand(result.getId()).toUri();

                //return new ResponseEntity<FileInfo>(fileInfo, headers, HttpStatus.OK);
                return ResponseEntity.created(location).body(result);


            } catch (Exception e) {
                return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
        }





    }
    */

    @RequestMapping(method = RequestMethod.GET, value="/{blogPostId}")
    BlogPost readBlogPost(@PathVariable Long blogPostId) {
        return this.blogPostRepository.findOne(blogPostId);
    }

    @RequestMapping(value="/fileupload", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
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

}
