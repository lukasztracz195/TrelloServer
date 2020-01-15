package pl.trello.dto.response.valid;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChangeTasksPositionsResponseDTO {

    private Long taskListId;

    private List<TaskDto> tasks;
}
