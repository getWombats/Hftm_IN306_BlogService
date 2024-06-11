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
        Log.info("Returning " + blogs.size() + " blogs.");
        return blogs;
    }

    public Blog getBlogById(long id) {
        return blogRepository.findById(id);
    }

    @Transactional
    public Blog addBlog(Blog blog) {

        blogRepository.persist(blog);

        Log.info("New blog with id " + blog.getId() + " successfully added.");

        return blog;
    }

    @Transactional
    public Blog deleteBlog(long id) {

        Blog blogToBeDeleted = blogRepository.findById(id);

        if (blogToBeDeleted != null) {
            blogRepository.delete(blogToBeDeleted);

            Log.info("Blog (id " + id + ") successfully deleted");

            return blogToBeDeleted;
        } else {
            Log.warn("Blog with id " + id + " not found. No deletion performed.");

            return null;
        }
    }

    @Transactional
    public Blog replaceBlog(Long id, Blog newBlog) {

        Blog existingBlog = blogRepository.findById(id);

        if (existingBlog != null) {

            existingBlog.setTitle(newBlog.getTitle());
            existingBlog.setContent(newBlog.getContent());
            blogRepository.persist(existingBlog);

            Log.info("Replaced blog with id " + id);

            return existingBlog;
        } else {
            Log.warn("Blog with id " + id + " not found. No replacement performed.");

            return null;
        }
    }

    @Transactional
    public Blog updateBlog(long id, Blog updatedBlog) {

        Blog blogToUpdate = blogRepository.findById(id);

        if (blogToUpdate != null) {
            if (!updatedBlog.getTitle().isEmpty()) {
                blogToUpdate.setTitle(updatedBlog.getTitle());
            }
            if (!updatedBlog.getContent().isEmpty()) {
                blogToUpdate.setContent(updatedBlog.getContent());
            }

            blogRepository.persist(blogToUpdate);

            Log.info("Blog with id " + id + " successfully updated.");

            return blogToUpdate;
        } else {
            Log.warn("Blog with id " + id + " not found. No update performed.");
        }

        return null;
    }
}
