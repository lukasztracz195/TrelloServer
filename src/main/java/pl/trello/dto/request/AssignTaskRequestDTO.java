package pl.trello.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignTaskRequestDTO {
    private Long taskId;

    private Long assignedUserId;

    private String principalUsername;


}
