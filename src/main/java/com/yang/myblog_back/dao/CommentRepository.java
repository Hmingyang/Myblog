package com.yang.myblog_back.dao;

import com.yang.myblog_back.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {



List<Comment> findByBlogId(Long blogId, Sort sort);

}
