package pl.trello.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class ChangeTasksPositionsRequest {

    private Long taskListId;

    private List<ChangeTasksPositionsRequest.TaskIdPosition> tasksIdPositions;

    private String login;

    @Getter
    @AllArgsConstructor
    public static class TaskIdPosition {

        private Long taskId;

        private Integer position;
    }
}
