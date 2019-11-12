package pl.trello.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MoveTaskRequestDTO {
    private Long taskId;
    private Long taskListId;
    private String username;
}
