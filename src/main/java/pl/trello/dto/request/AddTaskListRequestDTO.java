package pl.trello.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddTaskListRequestDTO {

    private String name;
}
