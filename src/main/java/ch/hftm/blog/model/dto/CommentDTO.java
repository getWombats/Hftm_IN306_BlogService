package ch.hftm.blog.model.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private Long blogId;
    private Integer commentNumber;
    @NotBlank(message = "Content must not be empty")
    private String content;
    private String author;
    private Instant createdAt;
    private Instant lastEditedAt;
}