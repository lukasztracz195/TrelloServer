package pl.trello.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddBoardRequest {

    private String boardName;

    private String login;
}
