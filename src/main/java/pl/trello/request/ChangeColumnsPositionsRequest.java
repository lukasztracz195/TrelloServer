package pl.trello.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ChangeColumnsPositionsRequest {

    private Long boardId;

    private List<TaskListIdPosition> taskListIdPositions;

    private String login;

    @Getter
    @AllArgsConstructor
    public static class TaskListIdPosition {

        private Long taskListId;

        private Integer position;
    }
}
