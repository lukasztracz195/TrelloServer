package pl.trello.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.trello.ResponseAdapter;
import pl.trello.dto.request.ChangeTaskListNameDTO;
import pl.trello.dto.request.GetTaskListByBoardDTO;
import pl.trello.dto.request.GetTaskListDTO;
import pl.trello.dto.response.valid.AddTaskListResponseDTO;
import pl.trello.dto.response.valid.AttachmentDto;
import pl.trello.dto.response.valid.ChangeColumnsPositionsResponseDto;
import pl.trello.dto.response.valid.GetTaskListResponse;
import pl.trello.dto.response.valid.GetTaskListsResponseDTO;
import pl.trello.dto.response.valid.TaskDto;
import pl.trello.entity.Board;
import pl.trello.entity.Comment;
import pl.trello.entity.Member;
import pl.trello.entity.Task;
import pl.trello.entity.TaskList;
import pl.trello.repository.BoardRepository;
import pl.trello.repository.TaskListRepository;
import pl.trello.repository.UserRepository;
import pl.trello.request.AddTaskListRequest;
import pl.trello.request.ChangeColumnsPositionsRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class TaskListService {

    private final BoardRepository boardRepository;

    private final TaskListRepository taskListRepository;

    private final UserRepository userRepository;

    private final BoardService boardService;

    public TaskListService(BoardRepository boardRepository,
                           TaskListRepository taskListRepository, UserRepository userRepository, BoardService boardService) {
        this.boardRepository = boardRepository;
        this.taskListRepository = taskListRepository;
        this.userRepository = userRepository;
        this.boardService = boardService;
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
                            .tasks(taskList.getTasks().stream().map(taskDto -> {
                                return TaskDto.builder()
                                        .taskId(taskDto.getTaskId())
                                        .date(TaskService.dateToString(taskDto.getDate()))
                                        .attachments(
                                                taskDto.getAttachments().stream().map(attachment ->
                                                        AttachmentDto.builder()
                                                        .attachmentId(attachment.getAttachmentId())
                                                        .name(attachment.getName())
                                                        .build()
                                                ).collect(Collectors.toList()))
                                        .comments(taskDto.getComments().stream().map(Comment::getCommentId)
                                                .collect(Collectors.toList()))
                                        .contractorId(createEmptyContractor(taskDto.getContractor()))
                                        .description(taskDto.getDescription())
                                        .reporterId(taskDto.getReporter().getMemberId())
                                        .build();
                            }
                            ).collect(Collectors.toList()))
                            .position(taskList.getPosition())
                            .name(taskList.getName())
                            .build())
                    .collect(Collectors.toList()));
        }
        return ResponseAdapter.notFound("Board with id:" + changeColumnsPositionsRequest.getBoardId() + " not exist");
    }

    public ResponseEntity changeColumnName(ChangeTaskListNameDTO changeTaskListNameDTO) {
        Optional<TaskList> optionalTaskList = taskListRepository.findById(changeTaskListNameDTO.getTaskListId());
        if (optionalTaskList.isPresent()) {
            Optional<Member> optionalMember = userRepository.findByUsername(changeTaskListNameDTO.getUsername());
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                List<Board> boards = boardService.findAllConnectedByMemberId(member.getMemberId());
                if (boards.contains(optionalTaskList.get().getBoard())) {
                    TaskList taskList = optionalTaskList.get();
                    if (!taskList.getName().equals(changeTaskListNameDTO.getName())) {
                        taskList.setName(changeTaskListNameDTO.getName());
                        taskListRepository.save(taskList);
                        return ResponseAdapter.ok();
                    }
                    return ResponseAdapter.forbidden("New and old name is the same");
                }
                return ResponseAdapter.forbidden("User have not privileges to this board");
            }
            return ResponseAdapter.forbidden("User about name" + changeTaskListNameDTO.getUsername() + "not exist");
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
                    return ResponseAdapter.ok(GetTaskListResponse.builder()
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


    public ResponseEntity getTaskListByBoard(GetTaskListByBoardDTO getTaskListByBoardDTO) {
        Optional<Board> optionalBoard = boardRepository.findById(getTaskListByBoardDTO.getBoardId());
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            Optional<Member> optionalMember = userRepository.findByUsername(getTaskListByBoardDTO.getUsername());
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                if (board.getOwner().equals(member) || board.getMembers().stream().anyMatch(f -> f.equals(member))) {
                    return ResponseAdapter.ok(GetTaskListsResponseDTO.builder()
                            .taskLists(prepareTaskLists(board.getTaskLists()))
                            .build());
                }
                return ResponseAdapter.forbidden("User have not privileges to this board");
            }
            return ResponseAdapter.notFound("User with name: " + getTaskListByBoardDTO.getUsername() + " not exist");
        }
        return ResponseAdapter.notFound("Board with id: " + getTaskListByBoardDTO.getBoardId() + " not exist");
    }

    private List<GetTaskListResponse> prepareTaskLists(List<TaskList> taskList) {
        List<GetTaskListResponse> list = new ArrayList<>();
        for (TaskList item : taskList) {
            TaskList innerTaskList = taskListRepository.findById(item.getTaskListId()).get();
            list.add(GetTaskListResponse.builder()
                    .boardId(innerTaskList.getBoard().getBoardId())
                    .name(innerTaskList.getName())
                    .tasks(innerTaskList.getTasks().stream().map(Task::getTaskId).collect(Collectors.toList()))
                    .position(innerTaskList.getPosition())
                    .taskListId(innerTaskList.getTaskListId())
                    .build());
        }
        return list;
    }

    private Long createEmptyContractor(Member contractor) {
        if (contractor != null) {
            return contractor.getMemberId();
        }
        return (long) -1;
    }
}
