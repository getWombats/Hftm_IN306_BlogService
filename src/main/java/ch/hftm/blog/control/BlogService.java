package ch.hftm.blog.control;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ch.hftm.blog.model.dto.BlogDTO;
import ch.hftm.blog.model.entity.Blog;
import ch.hftm.blog.repository.BlogRepository;
import ch.hftm.blog.repository.CommentRepository;
import ch.hftm.blog.service.DtoConverter;
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

    @Inject
    CommentRepository commentRepository;

    @Inject
    DtoConverter dtoConverter;

    public List<BlogDTO> getBlogs() {
        return this.getBlogs(Optional.empty(), Optional.empty());
    }

    public List<BlogDTO> getBlogs(Optional<String> searchString, Optional<Long> page) {
        PanacheQuery<Blog> blogQuery;
        if (searchString.isEmpty()) {
            blogQuery = blogRepository.findAll();
        } else {
            blogQuery = blogRepository.find("title like ?1 or content like ?1", "%" + searchString.get() + "%");
        }

        List<Blog> blogs = blogQuery.page(Page.ofSize(20)).list();
        return dtoConverter.toBlogDtoCollection(blogs);
    }

    public BlogDTO getBlogById(long blogId) {
        return dtoConverter.toBlogDto(blogRepository.findById(blogId));
    }

    @Transactional
    public BlogDTO addBlog(BlogDTO blogDto) {
        blogDto.setCreatedAt(LocalDateTime.now());

        try{
            blogRepository.persist(dtoConverter.fromBlogDto(blogDto));
        }
        catch (Exception e){
            Log.error("Error while persisting blog: " + e.getMessage());
            return null;
        }

        return blogDto;
    }

    @Transactional
    public BlogDTO deleteBlog(long blogId) {

        Blog blogToBeDeleted = blogRepository.findById(blogId);

        if (blogToBeDeleted == null) {
            Log.warn("Blog with id " + blogId + " not found. No deletion performed.");
            return null;
        }

        blogRepository.delete(blogToBeDeleted);
        return dtoConverter.toBlogDto(blogToBeDeleted);
    }

    @Transactional
    public BlogDTO replaceBlog(Long blogId, BlogDTO newBlog) {

        Blog existingBlog = blogRepository.findById(blogId);

        if (existingBlog == null) {
            Log.warn("Blog with id " + blogId + " not found. No replacement performed.");
            return null;
        }

        existingBlog.setTitle(newBlog.getTitle());
        existingBlog.setContent(newBlog.getContent());
        existingBlog.setAuthorName(newBlog.getAuthorName());
        existingBlog.setCreatedAt(newBlog.getCreatedAt());
        existingBlog.setLastEditedAt(newBlog.getLastEditedAt());
        blogRepository.persist(existingBlog);

        return dtoConverter.toBlogDto(existingBlog);
    }

    @Transactional
    public BlogDTO updateBlog(long blogId, BlogDTO updatedBlog) {

        Blog blogToUpdate = blogRepository.findById(blogId);

        if (blogToUpdate == null){
            Log.warn("Blog with id " + blogId + " not found. No update performed.");
            return null;
        }

        updatedBlog.setLastEditedAt(LocalDateTime.now());

        blogToUpdate.setTitle(updatedBlog.getTitle());
        blogToUpdate.setContent(updatedBlog.getContent());
        blogToUpdate.setLastEditedAt(updatedBlog.getLastEditedAt());

        blogRepository.persist(blogToUpdate);
        return dtoConverter.toBlogDto(blogToUpdate);
    }
}
