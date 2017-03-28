package blog;


import javax.persistence.*;
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

    public BlogPost(String postText, Date postedDate, String author, String title, String imageLocation) {
        this.postText = postText;
        this.postedDate = postedDate;
        this.author = author;
        this.title = title;
        this.imageLocation = imageLocation;
    }

    @Lob
    public String postText;

    public Date postedDate;
    public String author;
    public String title;
    public String imageLocation;

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

    public String getImageLocation() { return imageLocation; }





}
