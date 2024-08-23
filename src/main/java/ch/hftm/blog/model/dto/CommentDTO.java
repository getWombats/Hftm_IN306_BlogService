package ch.hftm.blog.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    @NotBlank(message = "Author name must not be empty")
    private String authorName;
    @NotBlank(message = "Content must not be empty")
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastEditedAt;
}
