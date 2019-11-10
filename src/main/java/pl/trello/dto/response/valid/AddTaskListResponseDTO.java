package pl.trello.dto.response.valid;

import lombok.Builder;
import lombok.Getter;
import pl.trello.entity.Task;

import java.util.List;

@Getter
@Builder
public class AddTaskListResponseDTO {

    private Long taskListId;

    private Long boardId;

    private String name;

    private Integer position;
}
