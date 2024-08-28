package ch.hftm.blog.control;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ch.hftm.blog.model.domain.ServiceResponse;
import ch.hftm.blog.model.dto.CommentDTO;
import ch.hftm.blog.model.entity.BlogPost;
import ch.hftm.blog.model.entity.Comment;
import ch.hftm.blog.repository.BlogPostRepository;
import ch.hftm.blog.repository.CommentRepository;
import ch.hftm.blog.util.BncConverter;
import ch.hftm.blog.util.RequestType;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CommentService extends ServiceBase {
    @Inject
    CommentRepository commentRepository;

    @Inject
    BlogPostRepository blogRepository;

    public ServiceResponse<List<CommentDTO>> getAllCommentsWithOptionalFiltering(Optional<String> searchString,
            Optional<Long> page) {
        ServiceResponse<List<CommentDTO>> serviceResponse = new ServiceResponse<>(RequestType.GET);
        PanacheQuery<Comment> commentQuery;

        try {
            if (searchString == null || searchString.isEmpty()) {
                commentQuery = commentRepository.findAll();
            } else {
                commentQuery = commentRepository.find("content like ?1", "%" + searchString.get() + "%");
            }
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while getting comments.");
            return serviceResponse;
        }

        List<Comment> comments = commentQuery.page(Page.ofSize(20)).list();
        serviceResponse.setData(BncConverter.toCommentDtoCollection(comments));
        return serviceResponse;
    }

    public ServiceResponse<List<CommentDTO>> getAllCommentsFromBlog(long blogId) {
        ServiceResponse<List<CommentDTO>> serviceResponse = new ServiceResponse<>(RequestType.GET);

        List<Comment> comments;

        try {
            BlogPost blog = blogRepository.findById(blogId);

            if (blog == null) {
                return serviceResponse;
            }

            comments = blog.getComments();
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while getting comments from blog with id " + blogId);
            return serviceResponse;
        }

        if (comments == null) {
            return serviceResponse;
        }

        serviceResponse.setData(BncConverter.toCommentDtoCollection(comments));
        return serviceResponse;
    }

    public ServiceResponse<CommentDTO> getSingleCommentFromBlog(long blogId, long commentId) {
        ServiceResponse<CommentDTO> serviceResponse = new ServiceResponse<>(RequestType.GET);

        if (blogRepository.findById(blogId) == null) {
            return serviceResponse;
        }

        Comment existingComment;

        try {
            existingComment = commentRepository.findById(commentId);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while getting comment with id " + commentId + " for updating.");
            return serviceResponse;
        }

        if (existingComment == null) {
            return serviceResponse;
        }

        serviceResponse.setData(BncConverter.toCommentDto(existingComment));
        return serviceResponse;
    }

    public ServiceResponse<CommentDTO> getCommentById(long commentId) {
        ServiceResponse<CommentDTO> serviceResponse = new ServiceResponse<>(RequestType.GET);

        Comment existingComment;

        try {
            existingComment = commentRepository.findById(commentId);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while getting comment with id " + commentId);
            return serviceResponse;
        }

        if (existingComment == null) {
            return serviceResponse;
        }

        serviceResponse.setData(BncConverter.toCommentDto(existingComment));
        return serviceResponse;
    }

    @Transactional
    public ServiceResponse<CommentDTO> addCommentToBlog(long blogId, CommentDTO commentDTO) {
        ServiceResponse<CommentDTO> serviceResponse = new ServiceResponse<>(RequestType.POST);

        BlogPost existingBlog;

        try {
            existingBlog = blogRepository.findById(blogId);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while getting blog with id " + blogId + " for adding comment.");
            return serviceResponse;
        }

        if (existingBlog == null) {
            return serviceResponse;
        }

        // Get comment count per blog to set the next comment number
        int commentCount;
        List<Comment> existingComments = existingBlog.getComments();
        if (existingComments == null) {
            commentCount = 0;
        } else {
            commentCount = existingComments.size();
        }

        // ? hibernate error / bug?
        // ? Converting blog with BncConverter as static or service throws
        // ? IllegalArgumentException
        // ! Message:
        // ! Class 'class ch.hftm.blog.util.BncConverter$5' is not an entity
        // ! classontext-propagation
        // Comment comment = BncConverter.toCommentEntity(commentDTO);

        Comment comment = new Comment();
        comment.setCommentNumber(commentCount + 1); // To show like a #1, #2, #3, etc. in a UI
        comment.setContent(commentDTO.getContent());
        comment.setAuthor(commentDTO.getAuthor());
        comment.setCreatedAt(Instant.now());
        comment.setBlog(existingBlog); // Set blog reference -> important for db relations!

        try {
            commentRepository.persist(comment);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while persisting comment.");
            return serviceResponse;
        }

        if (commentRepository.isPersistent(comment)) {
            serviceResponse.setData(BncConverter.toCommentDto(comment));
        }

        return serviceResponse;
    }

    @Transactional
    public ServiceResponse<CommentDTO> updateCommentOfBlog(long blogId, CommentDTO commentDTO) {
        ServiceResponse<CommentDTO> serviceResponse = new ServiceResponse<>(RequestType.PATCH);
        long commentId = commentDTO.getId();

        BlogPost corresponBlog = blogRepository.findById(blogId);

        if (corresponBlog == null) {
            return serviceResponse;
        }

        Comment existingComment;

        try {
            existingComment = commentRepository.findById(commentId);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while getting comment with id " + commentId + " for updating.");
            return serviceResponse;
        }

        if (existingComment == null) {
            return serviceResponse;
        }

        existingComment.setContent(commentDTO.getContent());
        existingComment.setLastEditedAt(Instant.now());

        try {
            commentRepository.persist(existingComment);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while updating comment.");
            return serviceResponse;
        }

        if (commentRepository.isPersistent(existingComment)) {
            serviceResponse.setData(BncConverter.toCommentDto(existingComment));
        }

        return serviceResponse;
    }

    @Transactional
    public ServiceResponse<CommentDTO> deleteCommentFromBlog(long blogId, long commentId) {
        ServiceResponse<CommentDTO> serviceResponse = new ServiceResponse<>(RequestType.DELETE);

        if (blogRepository.findById(blogId) == null) {
            return serviceResponse;
        }

        Comment existingComment;

        try {
            existingComment = commentRepository.findById(commentId);
        } catch (Exception ex) {
            handleError(serviceResponse, ex,
                    "Error while getting comment with id " + commentId + " for deleting from blog with id " + blogId);
            return serviceResponse;
        }

        if (existingComment == null) {
            return serviceResponse;
        }

        try {
            commentRepository.deleteById(commentId);
        } catch (Exception ex) {
            handleError(serviceResponse, ex, "Error while deleting comment with id " + commentId);
            return serviceResponse;
        }

        serviceResponse.setData(BncConverter.toCommentDto(existingComment));
        return serviceResponse;
    }
}