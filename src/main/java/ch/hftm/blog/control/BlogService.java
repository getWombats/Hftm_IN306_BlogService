package ch.hftm.blog.control;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
        Blog foundBlog = blogRepository.findById(blogId);

        if (foundBlog == null) {
            Log.warn("Blog with id " + blogId + " not found.");
            return null;
        }

        return dtoConverter.toBlogDto(foundBlog);
    }

    @Transactional
    public BlogDTO addBlog(BlogDTO blogDto) {

        blogDto.setCreatedAt(LocalDateTime.now());

        try {
            blogRepository.persist(dtoConverter.fromBlogDto(blogDto));
        } catch (Exception e) {
            Log.error("Error while persisting blog:\n" + e.getClass().getName() + "\n" + e.getMessage());
            return null;
        }

        Blog addedBlog = blogRepository.find("title = ?1 and content = ?2 and authorName = ?3",
                blogDto.getTitle(), blogDto.getContent(), blogDto.getAuthorName()).firstResult();

        if (addedBlog == null) {
            Log.error("Error while retrieving added blog.");
            return null;
        }

        return dtoConverter.toBlogDto(addedBlog);
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
    public BlogDTO replaceBlog(Long replaceBlogId, Long replacementBlogId) {

        Blog existingBlog = blogRepository.findById(replaceBlogId);

        if (existingBlog == null) {
            Log.warn("Blog to replace with id " + replaceBlogId + " not found. No replacement performed.");
            return null;
        }

        Blog replacementBlog = blogRepository.findById(replacementBlogId);

        if (replacementBlog == null) {
            Log.warn("Replacing blog with id " + replacementBlogId + " not found. No replacement performed.");
            return null;
        }

        existingBlog.setTitle(replacementBlog.getTitle());
        existingBlog.setContent(replacementBlog.getContent());
        existingBlog.setAuthorName(replacementBlog.getAuthorName());
        existingBlog.setCreatedAt(replacementBlog.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());

        if(replacementBlog.getLastEditedAt() != null){
            existingBlog.setLastEditedAt(replacementBlog.getLastEditedAt().atZone(ZoneId.systemDefault()).toInstant());
        } else {
            existingBlog.setLastEditedAt(null);
        }
        
        blogRepository.persist(existingBlog);

        return dtoConverter.toBlogDto(existingBlog);
    }

    @Transactional
    public BlogDTO updateBlog(long blogId, BlogDTO updatedBlog) {

        Blog blogToUpdate = blogRepository.findById(blogId);

        if (blogToUpdate == null) {
            Log.warn("Blog with id " + blogId + " not found. No update performed.");
            return null;
        }

        updatedBlog.setLastEditedAt(LocalDateTime.now());

        blogToUpdate.setTitle(updatedBlog.getTitle());
        blogToUpdate.setContent(updatedBlog.getContent());
        blogToUpdate.setLastEditedAt(updatedBlog.getLastEditedAt().atZone(ZoneId.systemDefault()).toInstant());

        blogRepository.persist(blogToUpdate);
        return dtoConverter.toBlogDto(blogToUpdate);
    }
}
