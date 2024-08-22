package ch.hftm.blog.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {
    private Long id;
    @NotBlank(message="Empty title not allowed")
    private String title;
    @NotBlank(message="Empty content not allowed")
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime lastEditedAt;
    private List<CommentDTO> comments;
}
