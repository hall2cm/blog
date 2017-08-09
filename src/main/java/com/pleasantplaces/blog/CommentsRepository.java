package com.pleasantplaces.blog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by cohall on 3/31/2017.
 */
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    List<Comments> findByBlogPostIdOrderByCommentDate(Long Id);

}
