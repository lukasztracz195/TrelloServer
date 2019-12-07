package pl.trello.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetTaskListByBoardDTO {
    private String username;
    private Long boardId;
}
