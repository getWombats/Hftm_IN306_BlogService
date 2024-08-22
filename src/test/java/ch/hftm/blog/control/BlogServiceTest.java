package ch.hftm.blog.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.hftm.blog.model.entity.Blog;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class BlogServiceTest {
    @Inject
    BlogService blogService;

    @Test
    void listingAndAddingBlog() {
        // Arrange
        Blog blog = new Blog("Testing Blog", "This is my testing blog");
        int blogsBefore;
        List<Blog> blogs;

        // Act
        blogsBefore = blogService.getBlogs().size();
        blogService.addBlog(blog);
        blogs = blogService.getBlogs();

        // Assert
        assertEquals(blogsBefore + 1, blogs.size());
        assertEquals(blog, blogs.get(blogs.size() - 1));
    }

    @Test
    void deletingBlog() {
        // Arrange
        Blog blog = new Blog("Testing Blog", "This is my testing blog");
        int blogsBefore;
        List<Blog> blogs;

        // Act
        blogsBefore = blogService.getBlogs().size();
        blogService.addBlog(blog);
        blogService.deleteBlog(1);
        blogs = blogService.getBlogs();

        // Assert
        assertEquals(blogsBefore - 1, blogs.size());
    }

    @Test
    void replacingBlog() {
        // Arrange
        Blog existingBlog = new Blog("Existing Blog", "This is an existing blog");
        Blog newBlog = new Blog("New Blog", "This is a new blog");

        // Add the existing blog
        blogService.addBlog(existingBlog);

        // Act
        Blog replacedBlog = blogService.replaceBlog(existingBlog.getId(), newBlog);

        // Assert
        assertNotNull(replacedBlog);
        assertEquals(newBlog.getTitle(), replacedBlog.getTitle());
        assertEquals(newBlog.getContent(), replacedBlog.getContent());
        assertEquals(existingBlog.getId(), replacedBlog.getId());
    }

    @Test
    void updatingBlog() {
    // Arrange
    Blog existingBlog = new Blog("Existing Blog", "This is an existing blog");
    Blog updatedBlog = new Blog("Updated Blog", "This is an updated blog");

    // Add the existing blog
    blogService.addBlog(existingBlog);

    // Act
    Blog updated = blogService.updateBlog(existingBlog.getId(), updatedBlog);

    // Assert
    assertNotNull(updated);
    assertEquals(updatedBlog.getTitle(), updated.getTitle());
    assertEquals(updatedBlog.getContent(), updated.getContent());
    assertEquals(existingBlog.getId(), updated.getId());
}
}
