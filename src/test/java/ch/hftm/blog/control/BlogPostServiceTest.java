package ch.hftm.blog.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import ch.hftm.blog.model.dto.BlogPostDTO;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@Disabled
@QuarkusTest
@TestInstance(Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlogPostServiceTest {
    private static final String NEW_BLOG_TITLE = "Ich und mein Quarkus";
    private static final String NEW_BLOG_CONTENT = "Quarkus ist ein Framework, das auf Java basiert und für die Entwicklung von Cloud-nativen Anwendungen optimiert ist. ROUNDHOUSE KICK!";
    private static final String NEW_BLOG_AUTHOR = "Chuck Norris";

    private int initialBlogsCount;

    @Inject
    BlogPostService blogService;

    @Test
    @TestTransaction
    void listingAndAddingBlog() {
        // ! Comparing blogPost objects not possible. creation time is set dynamically
        // on persistence

        // Arrange
        BlogPostDTO blogPostDTO = new BlogPostDTO(null, NEW_BLOG_TITLE, NEW_BLOG_CONTENT, NEW_BLOG_AUTHOR, null, null, null);
        BlogPostDTO addedBlogPost;
        List<BlogPostDTO> blogs;

        // Act
        List<BlogPostDTO> existingBlogs = blogService.getAllBlogPostsWithOptionalFiltering(null, null).getData();
        if (existingBlogs == null) {
            initialBlogsCount = 0;
        } else {
            initialBlogsCount = existingBlogs.size();
        }

        addedBlogPost = blogService.addBlogPost(blogPostDTO).getData();
        blogs = blogService.getAllBlogPostsWithOptionalFiltering(null, null).getData();

        // Assert
        assertNotNull(addedBlogPost);
        assertEquals(initialBlogsCount + 1, blogs.size());
    }

    @Test
    @TestTransaction
    void addingAndGettingBlogById() {
        // Arrange
        BlogPostDTO blogDTO = new BlogPostDTO(null, NEW_BLOG_TITLE, NEW_BLOG_CONTENT, NEW_BLOG_AUTHOR, null, null, null);

        // Act
        BlogPostDTO addedBlogPost = blogService.addBlogPost(blogDTO).getData();
        BlogPostDTO foundBlogDTO = blogService.getBlogById(addedBlogPost.getId()).getData();

        // Assert
        assertNotNull(foundBlogDTO);
        assertEquals(blogDTO.getTitle(), foundBlogDTO.getTitle());
        assertEquals(blogDTO.getContent(), foundBlogDTO.getContent());
    }

    @Test
    @TestTransaction
    void deletingBlog() {
        // Arrange
        BlogPostDTO blogPostDTO = new BlogPostDTO(null, NEW_BLOG_TITLE, NEW_BLOG_CONTENT, NEW_BLOG_AUTHOR, null, null, null);
        BlogPostDTO blogPostToBeDeleted;
        BlogPostDTO deletedBlogPost;
        int initialBlogsCount;
        List<BlogPostDTO> blogs;

        // Act
        blogService.addBlogPost(blogPostDTO).getData();
        initialBlogsCount = blogService.getAllBlogPostsWithOptionalFiltering(null, null).getData().size();
        blogPostToBeDeleted = blogService.addBlogPost(blogPostDTO).getData();
        deletedBlogPost = blogService.deleteBlogPost(blogPostToBeDeleted.getId()).getData();
        blogs = blogService.getAllBlogPostsWithOptionalFiltering(null, null).getData();

        // Assert
        assertNotNull(deletedBlogPost);
        assertEquals(initialBlogsCount, blogs.size());
        assertEquals(blogPostToBeDeleted, deletedBlogPost);
    }

    @Test
    @TestTransaction
    void updatingBlog() {
        // Arrange
        BlogPostDTO existingBlogPostDTO = new BlogPostDTO(null, NEW_BLOG_TITLE, NEW_BLOG_CONTENT, NEW_BLOG_AUTHOR, null, null, null);
        BlogPostDTO updatingBlogPostDTO = new BlogPostDTO(null, "Updated Blog", "This is an updated blog", NEW_BLOG_AUTHOR, null, null,
                null);
        BlogPostDTO updatedBlogPostDTO;

        // Act
        existingBlogPostDTO = blogService.addBlogPost(existingBlogPostDTO).getData();
        updatingBlogPostDTO.setId(existingBlogPostDTO.getId());
        updatedBlogPostDTO = blogService.updateBlogPost(updatingBlogPostDTO).getData();

        // Assert
        assertNotNull(updatedBlogPostDTO);
        assertEquals(updatingBlogPostDTO.getTitle(), updatedBlogPostDTO.getTitle());
        assertEquals(updatingBlogPostDTO.getContent(), updatedBlogPostDTO.getContent());
    }
}
