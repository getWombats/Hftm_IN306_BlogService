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
    @NotBlank(message="Empty author name not allowed")
    private String authorName;
    @NotBlank(message="Empty content not allowed")
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastEditedAt;
}

