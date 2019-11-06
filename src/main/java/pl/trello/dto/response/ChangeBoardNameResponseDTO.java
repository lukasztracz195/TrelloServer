package pl.trello.dto.response;

import lombok.Builder;
import pl.trello.entity.Member;
import pl.trello.entity.TaskList;

import java.util.List;

@Builder
public class ChangeBoardNameResponseDTO {

    private Long boardId;

    private Member owner;

    private List<Member> members;

    private List<TaskList> columns;

    private String name;
}
