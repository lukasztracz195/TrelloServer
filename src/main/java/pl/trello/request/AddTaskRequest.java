package pl.trello.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddTaskRequest {

    private Long taskListId;

    private String username;

    private String description;

    private Long dateInMilli;
}
