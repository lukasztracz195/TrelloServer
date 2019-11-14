package pl.trello.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddCommentRequestDTO {

    private Long taskId;
    private String username;
    private String content;
}
