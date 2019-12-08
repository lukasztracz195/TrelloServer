package pl.trello.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class AddTaskRequestDTO {

    private Long taskListId;

    private String description;

}
