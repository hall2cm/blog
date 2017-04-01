package blog;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by cohall on 3/30/2017.
 */

@Entity
public class User {

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<BlogPost> blogposts = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Comments> comments = new HashSet<>();


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    User() { //jpa only
    }

    public User(String displayName, String email, String photoUrl, String providerId, String uid, String role) {
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.providerId = providerId;
        this.uid = uid;
        this.role = role;
    }

    public String displayName;
    public String email;
    public String photoUrl;
    public String providerId;
    public String uid;
    public String role;


    public Set<BlogPost> getBlogposts() {
        return blogposts;
    }

    public Set<Comments> getComments() {
        return comments;
    }

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() { return email; }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getUid() { return uid; }

    public String getRole() { return role; }



}
