package pl.trello.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeTaskPositionsRequestDto {

    private Long taskId;

    private Integer position;
}
