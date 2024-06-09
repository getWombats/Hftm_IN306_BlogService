package ch.hftm.blog.control;

import java.util.List;
import java.util.Optional;

import ch.hftm.blog.entity.Blog;
import ch.hftm.blog.repository.BlogRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BlogService {
    @Inject
    BlogRepository blogRepository;

    public List<Blog> getBlogs() {
        return this.getBlogs(Optional.empty(), Optional.empty());
    }

    public List<Blog> getBlogs(Optional<String> searchString, Optional<Long> page) {
        PanacheQuery<Blog> blogQuery;
        if (searchString.isEmpty()) {
            blogQuery = blogRepository.findAll();
        } else {
            blogQuery = blogRepository.find("title like ?1 or content like ?1", "%" + searchString.get() + "%");
        }

        List<Blog> blogs = blogQuery.page(Page.ofSize(20)).list();
        Log.info("Returning " + blogs.size() + " blogs");
        return blogs;
    }

    public Blog getBlogById(long id) {
        return blogRepository.findById(id);
    }

    @Transactional
    public void addBlog(Blog blog) {
        Log.info("Adding blog " + blog.getTitle());
        blogRepository.persist(blog);
    }
}
