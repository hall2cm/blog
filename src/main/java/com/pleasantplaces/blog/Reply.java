package com.pleasantplaces.blog;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by chall on 6/6/2017.
 */

@Entity
public class Reply {

    @JsonIgnore
    @ManyToOne
    public Comments comments;

    @ManyToOne
    public User user;



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    Reply() { //jpa only
    }

    public Reply(Comments comments, User user, String replyText, Date replyDate) {
        this.replyText = replyText;
        this.replyDate = replyDate;
        this.comments = comments;
        this.user = user;

    }

    @Lob
    public String replyText;
    public Date replyDate;


    public Comments getComments() {
        return comments;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getreplyText() {
        return replyText;
    }

    public Date getreplyDate() { return replyDate; }


}
