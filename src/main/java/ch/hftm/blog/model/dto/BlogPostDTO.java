package ch.hftm.blog.model.dto;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogPostDTO {
    private Long id;
    @NotBlank(message = "Title must not be empty")
    private String title;
    @NotBlank(message = "Content must not be empty")
    private String content;
    private String author;
    private Instant createdAt;
    private Instant lastEditedAt;
    private List<CommentDTO> comments;
}
