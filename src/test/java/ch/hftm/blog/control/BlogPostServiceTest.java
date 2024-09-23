package ch.hftm.blog.control;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import ch.hftm.blog.model.dto.CommentDTO;
import ch.hftm.blog.model.entity.BlogPost;
import ch.hftm.blog.model.entity.Comment;
import ch.hftm.blog.repository.BlogPostRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import ch.hftm.blog.model.dto.BlogPostDTO;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@QuarkusTest
@TestInstance(Lifecycle.PER_METHOD)
public class BlogPostServiceTest {
    private static final String NEW_BLOG_TITLE = "Ich und mein Quarkus";
    private static final String NEW_BLOG_CONTENT = "Quarkus ist ein Framework, das auf Java basiert und für die Entwicklung von Cloud-nativen Anwendungen optimiert ist. ROUNDHOUSE KICK!";
    private static final String NEW_BLOG_AUTHOR = "Chuck Norris";

    @InjectMock
    BlogPostRepository blogPostRepositoryMock;

    @Inject
    BlogPostService blogService;

    @BeforeEach
    public void setUp() {
        @SuppressWarnings("unchecked")
        PanacheQuery<BlogPost> blogQueryMock = Mockito.mock(PanacheQuery.class);
        Mockito.when(blogQueryMock.page(Mockito.any())).thenReturn(blogQueryMock);
        Mockito.when(blogQueryMock.list()).thenReturn(List.of(new BlogPost(null, "title", "content", "author", Instant.now(), null, null)));
        Mockito.when(blogPostRepositoryMock.findAll()).thenReturn(blogQueryMock);
    }

    @Test
    void listingAndAddingBlog() {
        // Arrange
        int initialBlogsCount = 0;
        BlogPost blogPost = new BlogPost(1L, NEW_BLOG_TITLE, NEW_BLOG_CONTENT, NEW_BLOG_AUTHOR, Instant.now(), null, null);
        List<BlogPostDTO> blogs;

        // Act
        blogPostRepositoryMock.persist(blogPost);
        blogs = blogService.getAllBlogPostsWithOptionalFiltering(null, null).getData();

        // Assert
        assertEquals(initialBlogsCount + 1, blogs.size());
    }

    @Test
    void deletingBlog() {
        // Arrange
        int initialBlogsCount;
        BlogPost blogPost = new BlogPost(null, NEW_BLOG_TITLE, NEW_BLOG_CONTENT, NEW_BLOG_AUTHOR, Instant.now(), null, null);
        List<BlogPostDTO> blogs;

        // Act
        initialBlogsCount = blogService.getAllBlogPostsWithOptionalFiltering(null, null).getData().size();
        blogPostRepositoryMock.persist(blogPost);
        BlogPostDTO deletedBlog = blogService.deleteBlogPost(2L).getData();
        blogs = blogService.getAllBlogPostsWithOptionalFiltering(null, null).getData();

        // Assert
        assertNull(deletedBlog);
        assertEquals(initialBlogsCount, blogs.size());
    }

    @Test
    void updatingBlog() {
        // Arrange
        int initialBlogsCount;
        BlogPostDTO updateBlogPost = new BlogPostDTO(1L, "Updated Blog title", NEW_BLOG_CONTENT, NEW_BLOG_AUTHOR, null, null, null);
        BlogPostDTO updatedBlogPost;

        // Act
        updatedBlogPost = blogService.updateBlogPost(updateBlogPost).getData();

        // Assert
        assertEquals("Updated Blog title", updateBlogPost.getTitle());
    }
}
