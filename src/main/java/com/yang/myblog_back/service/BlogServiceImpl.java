package com.yang.myblog_back.service;

import com.yang.myblog_back.dao.BlogRepository;
import com.yang.myblog_back.po.Blog;
import com.yang.myblog_back.po.Type;
import com.yang.myblog_back.util.MarkdownUtils;
import com.yang.myblog_back.util.MyBeanUtils;
import com.yang.myblog_back.vo.BlogQuery;
import com.yang.myblog_back.web.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getById(id);
    }
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Transactional
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();
                if(!"".equals(blog.getTitle())&&blog.getTitle()!=null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId()!=null){
                    predicates.add(cb.equal(root. <Type>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);//??????????????????
        }
        else{
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }
    @Transactional
    @Override
    public Blog updateBlog(Blog blog,Long id) {
        Blog b=blogRepository.getById(id);
        if(b==null){
            throw new NotFoundException("??????????????????");

        }
        BeanUtils.copyProperties(blog,b,MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }
    @Transactional
    @Override
    public void deleteBlog(Long id) {
            blogRepository.deleteById(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
       return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable= PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog blog=blogRepository.getById(id);
        if(blog==null){
            throw new  NotFoundException("??????????????????");
        }
        Blog b= new Blog();
        BeanUtils.copyProperties(blog,b);
        String content =b.getContent();
       b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return blog;
    }
}
