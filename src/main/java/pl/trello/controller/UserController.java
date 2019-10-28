package pl.trello.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.trello.dto.request.AddUserRequest;
import pl.trello.service.UserService;

import static pl.trello.controller.UserController.USER_PATH;

@RestController
@RequestMapping(USER_PATH)
public class UserController {

    static final String USER_PATH = "/user";

    private static final String REGISTER_PATH = "/register";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            value = REGISTER_PATH,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity register(@RequestBody AddUserRequest request) {
        return userService.register(request);
    }

}
