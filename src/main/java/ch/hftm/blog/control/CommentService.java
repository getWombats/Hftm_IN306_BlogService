package ch.hftm.blog.control;

import java.time.LocalDateTime;

import ch.hftm.blog.model.entity.Blog;
import ch.hftm.blog.model.entity.Comment;
import ch.hftm.blog.repository.BlogRepository;
import ch.hftm.blog.repository.CommentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CommentService {
    @Inject
    CommentRepository commentRepository;

    @Inject
    BlogRepository blogRepository;

    public List<Comment> getAllComments() {
        return commentRepository.listAll();
    }

    public List<Comment> getCommentsFromBlogByBlogId(Long blogId) {

        Blog blog = blogRepository.findById(blogId);

        if (blog != null) {
            return blog.getComments();
        }

        return null;
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public Comment addComment(Comment comment, Long blogId) {

        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.persist(comment);

        Blog blog = blogRepository.findById(blogId);
        blog.getComments().add(comment);
        blogRepository.persist(blog);

        return comment;
    }

    @Transactional
    public Comment deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId);

        if (comment != null) {
            commentRepository.delete(comment);
        }

        return comment;
    }

    @Transactional
    public Comment updateComment(Comment updatedComment) {

        Comment existingComment = commentRepository.findById(updatedComment.getId());

        if (existingComment != null) {
            existingComment.setContent(updatedComment.getContent());
            existingComment.setLastEditedAt(LocalDateTime.now());
            commentRepository.persist(existingComment);
        }

        return existingComment;
    }
}
