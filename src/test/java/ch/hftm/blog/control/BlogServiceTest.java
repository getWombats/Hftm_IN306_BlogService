package ch.hftm.blog.control;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.hftm.blog.entity.Blog;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class BlogServiceTest {
    @Inject
    BlogService blogService;

    @Test
    void listingAndAddingBlogs() {
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
}
