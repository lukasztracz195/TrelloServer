package pl.trello.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddUserRequestDTO implements Request {

    private String login;

    private String password;
}
