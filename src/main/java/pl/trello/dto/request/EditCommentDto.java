package pl.trello.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EditCommentDto {

    private Long commentId;

    private String content;

    private String username;
}
