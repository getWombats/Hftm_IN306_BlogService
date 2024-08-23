package ch.hftm.blog.control;

import java.time.LocalDateTime;
import java.time.ZoneId;

import ch.hftm.blog.model.dto.CommentDTO;
import ch.hftm.blog.model.entity.Blog;
import ch.hftm.blog.model.entity.Comment;
import ch.hftm.blog.repository.BlogRepository;
import ch.hftm.blog.repository.CommentRepository;
import ch.hftm.blog.service.DtoConverter;
import io.quarkus.logging.Log;
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

    @Inject
    DtoConverter dtoConverter;

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
    public Comment addComment(CommentDTO comment, Long blogId) {

        comment.setCreatedAt(LocalDateTime.now());

        Comment commentToAdd = dtoConverter.fromCommentDto(comment);
        commentRepository.persist(commentToAdd);

        Comment addedComment = commentRepository.find("content = ?1 and createdAt = ?2", comment.getContent(),
                comment.getCreatedAt()).firstResult();

        if(addedComment == null) {
            Log.warn("Comment for blog with id " + blogId + " was not persisted.");
            return null;
        }

        Blog blog = blogRepository.findById(blogId);
        blog.getComments().add(commentToAdd);
        blogRepository.persist(blog);

        return addedComment;
    }

    @Transactional
    public CommentDTO deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId);

        if (comment != null) {
            commentRepository.delete(comment);
        }

        return dtoConverter.toCommentDto(comment);
    }

    @Transactional
    public CommentDTO updateComment(CommentDTO updatedComment) {

        Comment existingComment = commentRepository.findById(updatedComment.getId());

        if (existingComment != null) {
            existingComment.setContent(updatedComment.getContent());
            existingComment.setLastEditedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            commentRepository.persist(existingComment);
        }

        return dtoConverter.toCommentDto(existingComment);
    }
}
