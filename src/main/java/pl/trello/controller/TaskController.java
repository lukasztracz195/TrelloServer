package pl.trello.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.trello.dto.request.AddTaskRequestDTO;
import pl.trello.dto.request.AssignTaskRequestDTO;
import pl.trello.dto.request.EditTaskRequestDTO;
import pl.trello.dto.request.MoveTaskRequestDTO;
import pl.trello.request.AddTaskRequest;
import pl.trello.request.AssignTaskRequest;
import pl.trello.request.EditTaskRequest;
import pl.trello.request.MoveTaskRequest;
import pl.trello.service.TaskService;

import java.security.Principal;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static pl.trello.controller.TaskController.TASKS_PATH;

@RestController
@RequestMapping(TASKS_PATH)
public class TaskController {
    static final String TASKS_PATH = "/tasks";
    private static final String ADD_PATH = EMPTY;
    private static final String TASK_LIST_ID = "taskListId";
    private static final String TASK_LIST_ID_VARIABLE = "{" + TASK_LIST_ID + "}";
    private static final String TASK_ID = "taskId";
    private static final String TASK_ID_VARIABLE = "{" + TASK_ID + "}";
    private static final String EDIT_TASK_PATH = "/edit/" + TASK_ID_VARIABLE;
    private static final String ASSIGN_TASK = "/assign/" + TASK_ID_VARIABLE;
    private static final String MOVE_TASK = "/move/";

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(
            value = EMPTY,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity addTask(@PathVariable(TASK_LIST_ID) Long taskListId,
                                  @RequestBody AddTaskRequestDTO addTaskRequestDTO, Principal principal) {
        return taskService.addTask(AddTaskRequest.builder()
                .username(principal.getName())
                .description(addTaskRequestDTO.getDescription())
                .taskListId(taskListId)
                .build());
    }

    @PostMapping(
            value = EDIT_TASK_PATH,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity editTask(@PathVariable(TASK_ID) Long taskId, @RequestBody EditTaskRequest editTaskRequest, Principal principal) {
        return taskService.editTask(EditTaskRequestDTO.builder()
                .taskId(taskId)
                .username(principal.getName())
                .description(editTaskRequest.getDescription())
                .build());
    }

    @PostMapping(
            value = ASSIGN_TASK,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity assignTask(@PathVariable(TASK_ID) Long taskId, @RequestBody AssignTaskRequest assignTaskRequest, Principal principal) {
        return taskService.assignTask(AssignTaskRequestDTO.builder()
                .taskId(taskId)
                .assignedUsername(assignTaskRequest.getUsername())
                .principalUsername(principal.getName())
                .build());
    }

    @PostMapping(
            value = MOVE_TASK,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity moveTask(@RequestBody MoveTaskRequest moveTaskRequest, Principal principal) {
        return taskService.moveTask(MoveTaskRequestDTO.builder()
                .taskId(moveTaskRequest.getTaskId())
                .taskListId(moveTaskRequest.getTaskListId())
                .username(principal.getName())
                .build());
    }
}
