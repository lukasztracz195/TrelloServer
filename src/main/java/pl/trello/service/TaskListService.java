package pl.trello.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.trello.ResponseAdapter;
import pl.trello.dto.request.ChangeTaskListNameDTO;
import pl.trello.dto.request.GetTaskListDTO;
import pl.trello.dto.response.valid.AddTaskListResponseDTO;
import pl.trello.dto.response.valid.ChangeColumnsPositionsResponseDto;
import pl.trello.dto.response.valid.GetTaskLIstResponse;
import pl.trello.entity.Board;
import pl.trello.entity.Member;
import pl.trello.entity.Task;
import pl.trello.entity.TaskList;
import pl.trello.repository.BoardRepository;
import pl.trello.repository.TaskListRepository;
import pl.trello.repository.UserRepository;
import pl.trello.request.AddTaskListRequest;
import pl.trello.request.ChangeColumnsPositionsRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskListService {

    private final BoardRepository boardRepository;

    private final TaskListRepository taskListRepository;

    private final UserRepository userRepository;

    public TaskListService(BoardRepository boardRepository,
                           TaskListRepository taskListRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.taskListRepository = taskListRepository;
        this.userRepository = userRepository;
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
        Optional<Board> optionalBoard = boardRepository.findById(changeColumnsPositionsRequest.getBoardId());
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            Set<Long> setTaskListIds = board.getTaskLists().stream()
                    .map(TaskList::getTaskListId)
                    .collect(Collectors.toSet());
            List<Long> ids = changeColumnsPositionsRequest.getTaskListIdPositions().stream()
                    .map(ChangeColumnsPositionsRequest.TaskListIdPosition::getTaskListId)
                    .collect(Collectors.toList());

            LongSummaryStatistics idsStatictics = ids.stream().mapToLong(aLong -> aLong).summaryStatistics();
            LongSummaryStatistics longSummaryStatistics = setTaskListIds.stream().mapToLong(aLong -> aLong).summaryStatistics();
            if (idsStatictics.getMin() == longSummaryStatistics.getMax() && idsStatictics.getMin() == longSummaryStatistics.getMin()) {

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
            return ResponseAdapter.badRequest("Requst not contain information about position all taskLists in board");
        }
        return ResponseAdapter.notFound("Board with id:" + changeColumnsPositionsRequest.getBoardId() + " not exist");
    }

    public ResponseEntity changeColumnName(ChangeTaskListNameDTO changeTaskListNameDTO) {
        Optional<TaskList> optionalTaskList = taskListRepository.findById(changeTaskListNameDTO.getTaskListId());
        if (optionalTaskList.isPresent()) {
            TaskList taskList = optionalTaskList.get();
            if (!taskList.getName().equals(changeTaskListNameDTO.getName())) {
                taskList.setName(changeTaskListNameDTO.getName());
                taskListRepository.save(taskList);
                return ResponseAdapter.ok();
            }
            return ResponseAdapter.forbidden("New and old name is the same");
        }
        return ResponseAdapter.notFound("TaskList about id: " + changeTaskListNameDTO.getTaskListId() + " not exist");
    }

    public ResponseEntity getTaskList(GetTaskListDTO getTaskListDTO) {
        Optional<TaskList> optionalTaskList = taskListRepository.findById(getTaskListDTO.getTaskListId());
        if (optionalTaskList.isPresent()) {
            TaskList taskList = optionalTaskList.get();
            Board board = taskList.getBoard();
            Optional<Member> optionalMember = userRepository.findByUsername(getTaskListDTO.getUsername());
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                if (board.getOwner().equals(member) || board.getMembers().stream().anyMatch(f -> f.equals(member))) {
                    return ResponseAdapter.ok(GetTaskLIstResponse.builder()
                            .name(taskList.getName())
                            .boardId(board.getBoardId())
                            .taskListId(taskList.getTaskListId())
                            .tasks(taskList.getTasks().stream().map(Task::getTaskId).collect(Collectors.toList()))
                            .position(taskList.getPosition())
                            .build());
                }
                return ResponseAdapter.forbidden("User have not privileges to this board");
            }
            return ResponseAdapter.notFound("User with name: " + getTaskListDTO.getUsername() + " not exist");
        }
        return ResponseAdapter.notFound("TaskList with id: " + getTaskListDTO.getTaskListId() + " not exist");
    }
}
