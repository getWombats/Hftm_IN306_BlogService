package ch.hftm.blog.util;

import java.util.List;
import java.util.stream.Collectors;

import ch.hftm.blog.model.dto.BlogPostDTO;
import ch.hftm.blog.model.dto.CommentDTO;
import ch.hftm.blog.model.entity.BlogPost;
import ch.hftm.blog.model.entity.Comment;

public class BncConverter {

    public static List<BlogPostDTO> toBlogDtoCollection(
            List<BlogPost> blogs) {
        if (blogs == null || blogs.isEmpty()) {
            return null;
        }

        return blogs.stream().map(blog -> new BlogPostDTO() {
            {
                setId(blog.getId());
                setTitle(blog.getTitle());
                setContent(blog.getContent());
                setAuthor(blog.getAuthor());
                setCreatedAt(blog.getCreatedAt());
                setLastEditedAt(blog.getLastEditedAt());
                setComments(toCommentDtoCollection(blog.getComments()));
            }
        }).collect(Collectors.toList());
    }

    public static List<CommentDTO> toCommentDtoCollection(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return null;
        }

        return comments.stream().map(comment -> new CommentDTO() {
            {
                setId(comment.getId());
                setCommentNumber(comment.getCommentNumber());
                setBlogId(comment.getBlog().getId());
                setContent(comment.getContent());
                setAuthor(comment.getAuthor());
                setCreatedAt(comment.getCreatedAt());
                setLastEditedAt(comment.getLastEditedAt());
            }
        }).collect(Collectors.toList());
    }

    public static List<Comment> toCommentEntityCollection(List<CommentDTO> commentDTOs) {
        if (commentDTOs == null || commentDTOs.isEmpty()) {
            return null;
        }

        return commentDTOs.stream().map(commentDTO -> new Comment() {
            {
                setId(commentDTO.getId());
                setCommentNumber(commentDTO.getCommentNumber());
                setContent(commentDTO.getContent());
                setAuthor(commentDTO.getAuthor());
                setCreatedAt(commentDTO.getCreatedAt());
                setLastEditedAt(commentDTO.getLastEditedAt());
            }
        }).collect(Collectors.toList());
    }

    public static BlogPostDTO toBlogDto(BlogPost blog) {
        if (blog == null) {
            return null;
        }

        return new BlogPostDTO() {
            {
                setId(blog.getId());
                setTitle(blog.getTitle());
                setContent(blog.getContent());
                setAuthor(blog.getAuthor());
                setCreatedAt(blog.getCreatedAt());
                setLastEditedAt(blog.getLastEditedAt());
                setComments(toCommentDtoCollection(blog.getComments()));
            }
        };
    }

    public static BlogPost toBlogEntity(BlogPostDTO blogDTO) {
        if (blogDTO == null) {
            return null;
        }

        return new BlogPost() {
            {
                setId(blogDTO.getId());
                setTitle(blogDTO.getTitle());
                setContent(blogDTO.getContent());
                setAuthor(blogDTO.getAuthor());
                setCreatedAt(blogDTO.getCreatedAt());
                setLastEditedAt(blogDTO.getLastEditedAt());
                setComments(toCommentEntityCollection(blogDTO.getComments()));
            }
        };
    }

    public static CommentDTO toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        return new CommentDTO() {
            {
                setId(comment.getId());
                setBlogId(comment.getBlog().getId());
                setCommentNumber(comment.getCommentNumber());
                setContent(comment.getContent());
                setAuthor(comment.getAuthor());
                setCreatedAt(comment.getCreatedAt());
                setLastEditedAt(comment.getLastEditedAt());
            }
        };
    }

    public static Comment toCommentEntity(CommentDTO commentDTO) {
        if (commentDTO == null) {
            return null;
        }

        return new Comment() {
            {
                setId(commentDTO.getId());
                setCommentNumber(commentDTO.getCommentNumber());
                setContent(commentDTO.getContent());
                setAuthor(commentDTO.getAuthor());
                setCreatedAt(commentDTO.getCreatedAt());
                setLastEditedAt(commentDTO.getLastEditedAt());
            }
        };
    }

    public static List<Comment> setNewBlogReferenceOnComments(BlogPost newBlogForReference, List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return null;
        }

        for (Comment comment : comments) {
            comment.setBlog(newBlogForReference);
        }

        return comments;
    }
}
