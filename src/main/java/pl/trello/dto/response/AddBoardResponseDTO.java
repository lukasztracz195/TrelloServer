package pl.trello.dto.response;

import lombok.Builder;
import lombok.Getter;
import pl.trello.entity.Member;

@Builder
@Getter
public class AddBoardResponseDTO {

    private Long boardId;

    private Long ownerId;

    private String name;
}
