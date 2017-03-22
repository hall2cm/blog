package blog;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.standard.DateTimeContext;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by cohall on 3/21/2017.
 */

@Entity
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    BlogPost() { //jpa only
    }

    public BlogPost(String postText, Date postedDate, String author, String title) {
        this.postText = postText;
        this.postedDate = postedDate;
        this.author = author;
        this.title = title;
    }

    public String postText;
    public Date postedDate;
    public String author;
    public String title;

    public Long getId() {
        return id;
    }

    public String getPostText() {
        return postText;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }





}
