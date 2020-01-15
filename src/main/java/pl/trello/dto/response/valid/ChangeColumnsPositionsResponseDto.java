package pl.trello.dto.response.valid;

import lombok.Builder;
import lombok.Getter;
import pl.trello.entity.Task;

import java.util.List;

@Getter
@Builder
public class ChangeColumnsPositionsResponseDto {

    private Long taskListId;

    private Long boardId;

    //TODO change to avoid requrency
    private List<TaskDto> tasks;

    private String name;

    private Integer position;
}
