package com.pleasantplaces.blog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by chall on 6/6/2017.
 */
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByCommentsIdOrderByReplyDate(Long Id);

}
