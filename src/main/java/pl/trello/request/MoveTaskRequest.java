package pl.trello.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MoveTaskRequest {
    private Long taskId;
    private Long taskListId;
}
