package pl.trello.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChangeBoardNameRequest {

    private Long boardId;

    private String boardName;

    private String username;
}
