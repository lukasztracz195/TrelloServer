package pl.trello.dto.response;

import lombok.Builder;
import pl.trello.entity.Member;

@Builder
public class AddBoardResponseDTO {

    private Long boardId;

    private Member owner;

    private String name;
}
