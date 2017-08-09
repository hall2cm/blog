package com.pleasantplaces.blog;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cohall on 3/21/2017.
 */

@Entity
public class BlogPost {

    //@JsonIgnore
    @ManyToOne
    public User user;

    //@JsonIgnore
    @OneToMany(mappedBy = "blogPost")
    private Set<Comments> comments = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    BlogPost() { //jpa only
    }

    public BlogPost(User user, String postText, Date postedDate, String title, String imageLocation, String homeText) {
        this.postText = postText;
        this.postedDate = postedDate;
        this.title = title;
        this.imageLocation = imageLocation;
        this.user = user;
        this.homeText = homeText;
    }

    @Lob
    public String postText;

    public Date postedDate;
    public String title;
    public String imageLocation;

    @Lob
    public String homeText;

    public User getUser() {
        return user;
    }

    public Set<Comments> getComments() {
        return comments;
    }

    public Long getId() {
        return id;
    }

    public String getPostText() {
        return postText;
    }
    public void setPostText(String postText) {
        this.postText = postText;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLocation() { return imageLocation; }
    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getHomeText() { return homeText; }
    public void setHomeText(String homeText) {
        this.homeText = homeText;
    }





}
