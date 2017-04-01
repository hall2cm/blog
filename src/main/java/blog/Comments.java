package blog;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by cohall on 3/31/2017.
 */

@Entity
public class Comments {

    @JsonIgnore
    @ManyToOne
    public BlogPost blogPost;

    @ManyToOne
    public User user;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    Comments() { //jpa only
    }

    public Comments(BlogPost blogPost, User user, String commentText, Date commentDate) {
        this.commentText = commentText;
        this.commentDate = commentDate;
        this.blogPost = blogPost;
        this.user = user;

    }

    @Lob
    public String commentText;
    public Date commentDate;


    public BlogPost getBlogPost() {
        return blogPost;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getcommentText() {
        return commentText;
    }

    public Date getcommentDate() { return commentDate; }


}
