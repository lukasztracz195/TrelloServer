package pl.trello.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetTaskListDTO {

    private String username;

    private Long taskListId;
}
