package pl.trello.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static pl.trello.controller.TaskController.TASKS_PATH;

@RestController
@RequestMapping(TASKS_PATH)
public class TaskController {
    static final String TASKS_PATH = "/tasks";
    private static final String ADD_PATH = EMPTY;

}
