package ch.hftm.blog.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import ch.hftm.blog.model.dto.BlogPostDTO;
import ch.hftm.blog.model.dto.CommentDTO;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
@TestInstance(Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentServiceTest {
    private static final String NEW_COMMENT_CONTENT = "Woooowe such comment";
    private static final String NEW_BLOG_TITLE = "Ich und mein Quarkus";
    private static final String NEW_BLOG_CONTENT = "Quarkus ist ein Framework, das auf Java basiert und für die Entwicklung von Cloud-nativen Anwendungen optimiert ist. ROUNDHOUSE KICK!";
    private static final String NEW_BLOG_AUTHOR = "Chuck Norris";

    @Inject
    CommentService commentService;

    @Inject
    BlogPostService blogPostService;

    @Test
    @TestTransaction
    void listingAndAddingComment() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO(null, null, null, NEW_COMMENT_CONTENT, NEW_BLOG_AUTHOR, null, null);
        BlogPostDTO blogPostDTO = new BlogPostDTO(null, NEW_BLOG_TITLE, NEW_BLOG_CONTENT, NEW_BLOG_AUTHOR, null, null,
                null);
        BlogPostDTO addedBlogPost;
        int commentCountBefore;
        List<CommentDTO> comments;

        // Act
        if (commentService.getAllCommentsWithOptionalFiltering(null, null).getData() == null) {
            commentCountBefore = 0;
        } else {
            commentCountBefore = commentService.getAllCommentsWithOptionalFiltering(null, null).getData().size();
        }

        addedBlogPost = blogPostService.addBlogPost(blogPostDTO).getData();
        CommentDTO persistedComment = commentService.addCommentToBlog(addedBlogPost.getId(), commentDTO).getData();
        comments = commentService.getAllCommentsWithOptionalFiltering(null, null).getData();

        // Assert
        assertNotNull(addedBlogPost);
        assertNotNull(persistedComment);
        assertEquals(commentCountBefore + 1, comments.size());
        assertEquals(persistedComment.getContent(), comments.get(comments.size() - 1).getContent());
    }

    @Test
    @TestTransaction
    void addingAndGettingCommentById() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO(null, null, 0, NEW_COMMENT_CONTENT, NEW_BLOG_AUTHOR, null, null);
        BlogPostDTO blogPostDTO = new BlogPostDTO(null, NEW_BLOG_TITLE, NEW_BLOG_CONTENT, NEW_BLOG_AUTHOR, null, null,
                null);
        BlogPostDTO addedBlogPost;
        CommentDTO persistedComment;
        CommentDTO foundComment;

        // Act
        addedBlogPost = blogPostService.addBlogPost(blogPostDTO).getData();
        persistedComment = commentService.addCommentToBlog(addedBlogPost.getId(), commentDTO).getData();
        foundComment = commentService.getCommentById(persistedComment.getId()).getData();

        // Assert
        assertNotNull(addedBlogPost);
        assertNotNull(persistedComment);
        assertNotNull(foundComment);
        assertEquals(persistedComment.getContent(), foundComment.getContent());
    }

    @Test
    @TestTransaction
    void addingAndDeletingComment() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO(null, null, null, NEW_COMMENT_CONTENT, NEW_BLOG_AUTHOR, null, null);
        BlogPostDTO blogPostDTO = new BlogPostDTO(null, NEW_BLOG_TITLE, NEW_BLOG_CONTENT, NEW_BLOG_AUTHOR, null, null,
                null);
        BlogPostDTO addedBlogPost;
        CommentDTO commentToBeDeleted;
        int commentCountBefore;
        List<CommentDTO> comments;

        // Act
        addedBlogPost = blogPostService.addBlogPost(blogPostDTO).getData();
        commentService.addCommentToBlog(addedBlogPost.getId(), commentDTO).getData();
        commentCountBefore = commentService.getAllCommentsWithOptionalFiltering(null, null).getData().size();
        commentToBeDeleted = commentService.addCommentToBlog(addedBlogPost.getId(), commentDTO).getData();
        commentService.deleteCommentFromBlog(addedBlogPost.getId(), commentToBeDeleted.getId()).getData();
        comments = commentService.getAllCommentsWithOptionalFiltering(null, null).getData();

        // Assert
        assertNotNull(comments);
        assertEquals(commentCountBefore, comments.size());
    }
}
