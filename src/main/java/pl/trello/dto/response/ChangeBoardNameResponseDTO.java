package pl.trello.dto.response;

import lombok.Builder;
import lombok.Getter;
import pl.trello.entity.Member;
import pl.trello.entity.TaskList;

import java.util.List;

@Builder
@Getter
public class ChangeBoardNameResponseDTO {

    private Long boardId;

    private Long ownerId;

    private List<Member> members;

    private List<TaskList> columns;

    private String name;
}
