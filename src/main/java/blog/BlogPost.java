package blog;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by cohall on 3/21/2017.
 */

@Entity
public class BlogPost {

    @JsonIgnore
    @ManyToOne
    public User user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    BlogPost() { //jpa only
    }

    public BlogPost(User user, String postText, Date postedDate, String title, String imageLocation) {
        this.postText = postText;
        this.postedDate = postedDate;
        this.title = title;
        this.imageLocation = imageLocation;
        this.user = user;
    }

    @Lob
    public String postText;

    public Date postedDate;
    public String title;
    public String imageLocation;

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getPostText() {
        return postText;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getImageLocation() { return imageLocation; }





}
