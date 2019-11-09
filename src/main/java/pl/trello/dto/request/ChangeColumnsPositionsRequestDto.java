package pl.trello.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeColumnsPositionsRequestDto {

    private Long taskListId;

    private Integer position;
}
