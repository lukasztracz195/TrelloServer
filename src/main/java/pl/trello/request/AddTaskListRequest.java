package pl.trello.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddTaskListRequest {

    private Long boardId;

    private String name;

    private String login;
}
