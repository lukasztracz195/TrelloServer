package pl.trello.dto.response;

import lombok.Builder;
import lombok.Getter;
import pl.trello.entity.Board;

import java.util.List;

@Getter
@Builder
public class GetBoardsResponseDTO {

    List<Board> boards;
}
