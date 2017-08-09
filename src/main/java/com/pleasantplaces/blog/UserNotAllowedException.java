package com.pleasantplaces.blog;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by cohall on 3/30/2017.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotAllowedException extends RuntimeException{

    public UserNotAllowedException(String userId) {
        super("User '" + userId + "'does not have access to create blog posts.");
    }

}