package ch.hftm.blog.control;

import java.util.List;

import ch.hftm.blog.entity.Blog;
import ch.hftm.blog.repository.BlogRepository;
import io.quarkus.logging.Log;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Dependent
public class BlogService {
    @Inject
    BlogRepository blogRepository;

    public List<Blog> getBlogs() {
        var blogs = blogRepository.listAll();
        Log.info("Returning " + blogs.size() + " blogs");
        return blogs;
    }

    @Transactional
    public void addBlog(Blog blog) {
        Log.info("Adding blog " + blog.getTitle());
        blogRepository.persist(blog);
    }
}
