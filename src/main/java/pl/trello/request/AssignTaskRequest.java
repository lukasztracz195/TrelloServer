package pl.trello.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignTaskRequest {
    private Long taskId;
    private Long userId;
}
