package pl.trello.dto.response;

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
    private List<Task> tasks;

    private String name;

    private Integer position;
}
