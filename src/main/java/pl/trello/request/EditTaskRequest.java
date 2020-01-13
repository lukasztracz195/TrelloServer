package pl.trello.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditTaskRequest {
    private Long taskId;
    private String description;
    private Long dateInMillis;
}
