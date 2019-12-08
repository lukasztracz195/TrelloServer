package pl.trello.dto.response.valid;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Builder
@Getter
public class CommentDto {

    private Long commentId;

    private Long taskId;

    private Long ownerId;

    private List<AttachmentDto> attachments;

    private LocalDateTime createdAt;

    private String content;
}
