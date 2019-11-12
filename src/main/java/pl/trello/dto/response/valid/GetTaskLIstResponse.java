package pl.trello.dto.response.valid;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetTaskLIstResponse {

    private String name;

    private Long boardId;

    private Long taskListId;

    private List<Long> tasks;

    private Integer position;
}
