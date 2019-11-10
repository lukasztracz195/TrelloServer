package pl.trello.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.trello.dto.response.AddTaskListResponseDTO;
import pl.trello.dto.response.ChangeColumnsPositionsResponseDto;
import pl.trello.entity.Board;
import pl.trello.entity.TaskList;
import pl.trello.repository.BoardRepository;
import pl.trello.repository.TaskListRepository;
import pl.trello.request.AddTaskListRequest;
import pl.trello.request.ChangeColumnsPositionsRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskListService {

    private final BoardRepository boardRepository;

    private final TaskListRepository taskListRepository;

    public TaskListService(BoardRepository boardRepository,
                           TaskListRepository taskListRepository) {
        this.boardRepository = boardRepository;
        this.taskListRepository = taskListRepository;
    }

    public ResponseEntity addTaskList(AddTaskListRequest addTaskListRequest) {
        //TODO validate

        Board board = boardRepository.findById(addTaskListRequest.getBoardId()).get();
        TaskList taskList = TaskList.builder()
                .board(board)
                .name(addTaskListRequest.getName())
                .position(getFreePosition(board))
                .build();

        taskList = taskListRepository.save(taskList);
        board.getTaskLists().add(taskList);
        boardRepository.save(board);

        return ResponseEntity.ok(AddTaskListResponseDTO.builder()
                .taskListId(taskList.getTaskListId())
                .boardId(board.getBoardId())
                .name(taskList.getName())
                .position(taskList.getPosition())
                .build());
    }

    private int getFreePosition(Board board) {
        return board.getTaskLists().stream()
                .mapToInt(TaskList::getPosition)
                .max()
                .orElse(-1) + 1;
    }

    public ResponseEntity changeColumnsPositions(ChangeColumnsPositionsRequest changeColumnsPositionsRequest) {
        //TODO validate

        List<TaskList> savedTaskLists = new LinkedList<>();
        changeColumnsPositionsRequest.getTaskListIdPositions()
                .forEach(taskListIdPosition -> {
                    TaskList taskList = taskListRepository.findById(taskListIdPosition.getTaskListId()).get();
                    taskList.setPosition(taskListIdPosition.getPosition());
                    savedTaskLists.add(taskListRepository.save(taskList));
                });

        return ResponseEntity.ok(savedTaskLists.stream()
                .map(taskList -> ChangeColumnsPositionsResponseDto.builder()
                        .taskListId(taskList.getTaskListId())
                        .boardId(taskList.getBoard().getBoardId())
                        .tasks(taskList.getTasks())
                        .position(taskList.getPosition())
                        .name(taskList.getName())
                        .build())
                .collect(Collectors.toList()));
    }
}
