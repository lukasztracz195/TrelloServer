package pl.trello.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class EditCommentRequestDto {

    private Long commentId;
    private String content;
}
