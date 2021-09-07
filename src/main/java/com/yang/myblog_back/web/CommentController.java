package com.yang.myblog_back.web;

import com.yang.myblog_back.po.Comment;
import com.yang.myblog_back.service.BlogService;
import com.yang.myblog_back.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;
    @Value("${comment.avatar}")
    private String avatar;
    @GetMapping("/comments/{blogId}")
    public String comments( @PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        //blog页面下的commentList
        return "blog :: commentList";
    }
    @PostMapping("/comments")
    public String post(Comment comment){
        Long blogid =comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogid));
        comment.setAvatar(avatar);
        commentService.saveComment(comment);
        return "redirect:/cmomments/"+comment.getId();
    }
}
