package pl.trello.dto.response.valid;

import lombok.Builder;
import lombok.Getter;
import pl.trello.entity.TaskList;

import java.util.List;

@Builder
@Getter
public class BoardResponseDTO {

    private String name;
    private String owner;
    private List<String> membersNames;
    private List<TaskList> taskLists;

}
