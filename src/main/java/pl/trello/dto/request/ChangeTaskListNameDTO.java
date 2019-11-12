package pl.trello.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangeTaskListNameDTO {

    private Long taskListId;
    private String name;

}
