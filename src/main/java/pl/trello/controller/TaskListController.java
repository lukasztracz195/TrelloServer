package pl.trello.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.trello.dto.request.AddTaskListRequestDTO;
import pl.trello.dto.request.ChangeColumnsPositionsRequestDto;
import pl.trello.dto.request.ChangeTaskListNameDTO;
import pl.trello.dto.request.GetTaskListDTO;
import pl.trello.request.AddTaskListRequest;
import pl.trello.request.ChangeColumnsPositionsRequest;
import pl.trello.request.ChangeTaskListNameRequest;
import pl.trello.service.TaskListService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static pl.trello.controller.BoardController.BOARD_ID;
import static pl.trello.controller.BoardController.BOARD_ID_VARIABLE;
import static pl.trello.controller.TaskListController.TASK_LIST_PATH;

@RestController
@RequestMapping(TASK_LIST_PATH)
public class TaskListController {

    public static final String TASK_LIST_PATH = "/taskLists";
    private static final String TASK_LIST_ID = "taskListId";
    private static final String TASK_LIST_ID_VARIABLE = "{" + TASK_LIST_ID + "}";
    private static final String EMPTY = "";
    private static final String ADD_TASK_LIST_PATH = "/" + BOARD_ID_VARIABLE;
    private static final String CHANGE_TASK_LIST_POSITIONS_PATH = "/" + TASK_LIST_ID_VARIABLE + "/changePositions";
    private static final String CHANGE_TASK_LIST_NAME = "/" + TASK_LIST_ID_VARIABLE + "/" + TASK_LIST_ID_VARIABLE + "rename";
    private static final String GET_TASK_LIST = "/get/" + TASK_LIST_ID_VARIABLE;


    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @PostMapping(
            value = ADD_TASK_LIST_PATH,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity addTaskList(@PathVariable(BOARD_ID) Long boardId,
                                      @RequestBody AddTaskListRequestDTO addTaskListRequestDTO,
                                      Principal principal) {
        return taskListService.addTaskList(AddTaskListRequest.builder()
                .boardId(boardId)
                .name(addTaskListRequestDTO.getName())
                .login(principal.getName())
                .build());
    }

    @PatchMapping(
            value = CHANGE_TASK_LIST_POSITIONS_PATH,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity changeTaskListPositions(@PathVariable(BOARD_ID) Long boardId,
                                                  @RequestBody List<ChangeColumnsPositionsRequestDto> changeColumnsPositionsRequestDtos,
                                                  Principal principal) {
        return taskListService.changeColumnsPositions(ChangeColumnsPositionsRequest.builder()
                .boardId(boardId)
                .taskListIdPositions(changeColumnsPositionsRequestDtos.stream()
                        .map(dto -> new ChangeColumnsPositionsRequest.TaskListIdPosition(dto.getTaskListId(), dto.getPosition()))
                        .collect(Collectors.toList()))
                .login(principal.getName())
                .build());
    }

    @PatchMapping(
            value = CHANGE_TASK_LIST_NAME,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity changeTaskListName(@PathVariable(TASK_LIST_ID) Long taskListId,
                                             @RequestBody ChangeTaskListNameRequest changeTaskListNameRequest) {
        return taskListService.changeColumnName(ChangeTaskListNameDTO.builder()
                .taskListId(taskListId)
                .name(changeTaskListNameRequest.getName())
                .build());
    }

    @GetMapping(
            value = GET_TASK_LIST,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity getTaskList(@PathVariable(TASK_LIST_ID) Long taskListId, Principal principal) {
        return taskListService.getTaskList(GetTaskListDTO.builder()
                .taskListId(taskListId)
                .username(principal.getName())
                .build());
    }
}
