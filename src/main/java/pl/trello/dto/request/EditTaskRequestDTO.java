package pl.trello.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditTaskRequestDTO {

    private String username;
    private String description;
    private String date;
    private Long taskId;
}
