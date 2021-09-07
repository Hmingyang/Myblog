package com.yang.myblog_back.service;

import com.yang.myblog_back.po.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(Long id);
    Comment saveComment(Comment comment);
}
