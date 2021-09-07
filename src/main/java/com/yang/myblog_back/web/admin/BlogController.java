package com.yang.myblog_back.web.admin;

import com.yang.myblog_back.po.Blog;
import com.yang.myblog_back.po.User;
import com.yang.myblog_back.service.BlogService;
import com.yang.myblog_back.service.TagService;
import com.yang.myblog_back.service.TypeService;
import com.yang.myblog_back.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final  String INPUT="admin/bloginput";
    private static final  String LIST="admin/blog_manager";
    private static final  String REDIRECT_LIST="redirect:/admin/blog_manager";
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @GetMapping("/blog_manager")
    public String blogs(@PageableDefault(size=3,sort={"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, Model model, BlogQuery blog ){
       model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
    return LIST;
    }
    @PostMapping("/blog_manager/search")
    public String serach(@PageableDefault(size=2,sort={"updateTime"},direction = Sort.Direction.DESC)
                                     Pageable pageable, Model model, BlogQuery blog){
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blog_manager ::blogList";//返回页面中的部分片段（blogList）
    }
  @GetMapping("/blog_manager/input")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }
    @GetMapping("/blog_manager/{id}/input")
    public String  editInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog=blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }
    private void setTypeAndTag(Model model){
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("types",typeService.listType());
    }
    @PostMapping("/blog_manager")
    public String post(Blog blog, RedirectAttributes attributes,HttpSession session){
      blog.setUser((User)session.getAttribute("user"));
      blog.setType(typeService.getType(blog.getType().getId()));
      blog.setTags(tagService.listTag(blog.getTagIds()));
       Blog b;
       if(blog.getId()==null){
           b=blogService.saveBlog(blog);
       }else{
           b=blogService.updateBlog(blog,blog.getId());
       }
       if(b ==null){
           attributes.addFlashAttribute("message","操作失败");
       }else{
           attributes.addFlashAttribute("message","操作成功");
       }
        return REDIRECT_LIST;
    }
    @GetMapping("/blog_manager/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);

        attributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;
    }
}
