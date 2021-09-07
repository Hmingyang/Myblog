package com.yang.myblog_back.service;

import com.yang.myblog_back.po.Blog;
import com.yang.myblog_back.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {
    Blog getBlog(Long id);
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
    Blog saveBlog(Blog blog);
    Blog updateBlog(Blog blog,Long id);
    void deleteBlog(Long id);
    Page<Blog>listBlog(Pageable pageable);
    List<Blog> listRecommendBlogTop(Integer size);
    Page<Blog>listBlog(String query,Pageable pageable);
    Blog getAndConvert(Long id);


}
