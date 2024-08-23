package ch.hftm.blog.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import ch.hftm.blog.model.dto.BlogDTO;
import ch.hftm.blog.model.dto.CommentDTO;
import ch.hftm.blog.model.entity.Blog;
import ch.hftm.blog.model.entity.Comment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;

@ApplicationScoped
public class DtoConverter {

    public Blog fromBlogDto(BlogDTO dto) {
        Blog entity = new Blog();

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }

        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setAuthorName(dto.getAuthorName());

        if (dto.getCreatedAt() != null) {
            entity.setCreatedAt(dto.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
        }

        if (dto.getLastEditedAt() != null) {
            entity.setLastEditedAt(dto.getLastEditedAt().atZone(ZoneId.systemDefault()).toInstant());
        }

        if (dto.getComments() != null) {
            entity.setComments(dto.getComments().stream().map(this::fromCommentDto).toList());
        }

        return entity;
    }

    public BlogDTO toBlogDto(Blog entity) {
        @Valid
        BlogDTO dto = new BlogDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setAuthorName(entity.getAuthorName());
        dto.setCreatedAt(LocalDateTime.ofInstant(entity.getCreatedAt(), ZoneId.systemDefault()));

        if (entity.getLastEditedAt() != null) {
            dto.setLastEditedAt(LocalDateTime.ofInstant(entity.getLastEditedAt(), ZoneId.systemDefault()));
        }

        if (entity.getComments() != null) {
            dto.setComments(entity.getComments().stream().map(this::toCommentDto).toList());
        }

        return dto;
    }

    public Comment fromCommentDto(CommentDTO dto) {
        Comment entity = new Comment();

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }

        entity.setAuthorName(dto.getAuthorName());
        entity.setContent(dto.getContent());

        if (dto.getCreatedAt() != null) {
            entity.setCreatedAt(dto.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
        }

        if (dto.getLastEditedAt() != null) {
            entity.setLastEditedAt(dto.getLastEditedAt().atZone(ZoneId.systemDefault()).toInstant());
        }

        return entity;
    }

    public CommentDTO toCommentDto(Comment entity) {
        @Valid
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setAuthorName(entity.getAuthorName());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(LocalDateTime.ofInstant(entity.getCreatedAt(), ZoneId.systemDefault()));

        if (entity.getLastEditedAt() != null) {
            dto.setLastEditedAt(LocalDateTime.ofInstant(entity.getLastEditedAt(), ZoneId.systemDefault()));
        }

        return dto;
    }

    public List<BlogDTO> toBlogDtoCollection(List<Blog> blogs) {
        return blogs.stream().map(this::toBlogDto).toList();
    }

    public List<Blog> fromBlogDtoCollection(List<BlogDTO> blogDtos) {
        return blogDtos.stream().map(this::fromBlogDto).toList();
    }

    public List<CommentDTO> toCommentDtoCollection(List<Comment> comments) {
        return comments.stream().map(this::toCommentDto).toList();
    }

    public List<Comment> fromCommentDtoCollection(List<CommentDTO> commentDtos) {
        return commentDtos.stream().map(this::fromCommentDto).toList();
    }

}
