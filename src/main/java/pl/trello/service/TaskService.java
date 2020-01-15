package pl.trello.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.trello.ResponseAdapter;
import pl.trello.dto.request.AssignTaskRequestDTO;
import pl.trello.dto.request.ChangeTasksPositionsRequest;
import pl.trello.dto.request.EditTaskRequestDTO;
import pl.trello.dto.request.MoveTaskRequestDTO;
import pl.trello.dto.response.valid.AttachmentDto;
import pl.trello.dto.response.valid.ChangeColumnsPositionsResponseDto;
import pl.trello.dto.response.valid.ChangeTasksPositionsResponseDTO;
import pl.trello.dto.response.valid.TaskDto;
import pl.trello.entity.Board;
import pl.trello.entity.Comment;
import pl.trello.entity.Member;
import pl.trello.entity.Task;
import pl.trello.entity.TaskList;
import pl.trello.repository.BoardRepository;
import pl.trello.repository.TaskListRepository;
import pl.trello.repository.TaskRepository;
import pl.trello.repository.UserRepository;
import pl.trello.request.AddTaskRequest;
import pl.trello.request.ChangeColumnsPositionsRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final String DELETE_DATE = "DELETE_DATE";

    public TaskService(TaskRepository taskRepository, TaskListRepository taskListRepository, BoardRepository boardRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity addTask(AddTaskRequest addTaskRequest) {
        Optional<Member> optionalMember = userRepository.findByUsername(addTaskRequest.getUsername());
        if (optionalMember.isPresent()) {
            Optional<TaskList> optionalTaskList = taskListRepository.findById(addTaskRequest.getTaskListId());
            if (optionalTaskList.isPresent()) {
                TaskList taskList = optionalTaskList.get();
                Board board = taskList.getBoard();
                if (isOwner(board, optionalMember.get()) || isMemberOfBoard(board, optionalMember.get())) {
                    Task task = Task.builder()
                            .taskList(taskList)
                            .comments(new ArrayList<>())
                            .position(taskList.getTasks().size() + 1)
                            .description(addTaskRequest.getDescription())
                            .reporter(optionalMember.get())
                            .position(taskList.getTasks().size())
                            .build();
                    task = taskRepository.save(task);
                    taskList.getTasks().add(task);
                    taskListRepository.save(taskList);
                    List<AttachmentDto> attachmentDtoList = new ArrayList<>();
                    return ResponseAdapter.ok(TaskDto.builder()
                            .attachments(attachmentDtoList)
                            .date(dateToString(task.getDate()))
                            .position(task.getPosition())
                            .comments(task.getComments().stream().map(Comment::getCommentId).collect(Collectors.toList()))
                            .contractorId(createEmptyContractor(task.getContractor()))
                            .description(task.getDescription())
                            .reporterId(task.getReporter().getMemberId())
                            .taskId(task.getTaskId())
                            .taskListId(task.getTaskList().getTaskListId())
                            .build());
                }
                return ResponseAdapter.forbidden("User has not privileges to this board");
            }
            return ResponseAdapter.notFound("Not exist taskList");
        }
        return ResponseAdapter.notFound("Not exist member");
    }

    public ResponseEntity editTask(EditTaskRequestDTO editTaskRequestDTO) {
        Optional<Task> optionalTask = taskRepository.findById(editTaskRequestDTO.getTaskId());
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            TaskList taskList = task.getTaskList();
            Board board = taskList.getBoard();
            Optional<Member> optionalMember = userRepository.findByUsername(editTaskRequestDTO.getUsername());
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                if (isOwner(board, member) || isMemberOfBoard(board, member)) {
                    task.setDescription(editTaskRequestDTO.getDescription());
                    LocalDateTime dateToSave;
                    if (editTaskRequestDTO.getDate().endsWith(DELETE_DATE)) {
                        dateToSave = null;
                    } else {
                        try {
                            dateToSave = LocalDateTime.parse(editTaskRequestDTO.getDate());
                        } catch (DateTimeParseException e) {
                            dateToSave = task.getDate();
                        }
                    }
                    task.setDate(dateToSave);
                    task = taskRepository.save(task);
                    List<AttachmentDto> attachmentDtoList = new ArrayList<>();
                    return ResponseAdapter.ok(TaskDto.builder()
                            .attachments(attachmentDtoList)
                            .comments(task.getComments().stream().map(Comment::getCommentId).collect(Collectors.toList()))
                            .contractorId(createEmptyContractor(task.getContractor()))
                            .position(task.getPosition())
                            .date(dateToString(task.getDate()))
                            .description(task.getDescription())
                            .reporterId(task.getReporter().getMemberId())
                            .taskId(task.getTaskId())
                            .taskListId(task.getTaskList().getTaskListId())
                            .build());
                }
                return ResponseAdapter.forbidden("User has not privileges to this board");
            }
            return ResponseAdapter.notFound("User with username: " + editTaskRequestDTO.getUsername() + " not exist");
        }
        return ResponseAdapter.notFound("Task with id: " + editTaskRequestDTO.getTaskId() + " not exist");

    }

    public ResponseEntity changeTasksPositions(ChangeTasksPositionsRequest changeTasksPositionsRequest) {
        Optional<TaskList> optionalTaskList = taskListRepository.findById(changeTasksPositionsRequest.getTaskListId());
        if (optionalTaskList.isPresent()) {
            List<Task> savedTasks = new LinkedList<>();
            changeTasksPositionsRequest.getTasksIdPositions()
                    .forEach(taskIdPosition -> {
                        Task task = taskRepository.findById(taskIdPosition.getTaskId()).get();
                        task.setPosition(taskIdPosition.getPosition());
                        savedTasks.add(taskRepository.save(task));
                    });
            savedTasks.sort(Comparator.comparingInt(Task::getPosition));
            List<TaskDto> list = new ArrayList<>();
            for (Task task : savedTasks) {
                TaskDto build = TaskDto.builder()
                        .taskId(task.getTaskId())
                        .position(task.getPosition())
                        .reporterId(task.getReporter().getMemberId())
                        .description(task.getDescription())
                        .taskListId(optionalTaskList.get().getTaskListId())
                        .contractorId(createEmptyContractor(task.getContractor()))
                        .comments(task.getComments().stream().map(
                                Comment::getCommentId).collect(Collectors.toList()))
                        .attachments(task.getAttachments().stream().map(attachment ->
                                AttachmentDto.builder()
                                        .name(attachment.getName())
                                        .attachmentId(attachment.getAttachmentId())
                                        .build()).collect(Collectors.toList()))
                        .date(dateToString(task.getDate()))
                        .build();
                list.add(build);
            }
            return ResponseEntity.ok(
                    ChangeTasksPositionsResponseDTO.builder()
                            .taskListId(optionalTaskList.get().getTaskListId())
                            .tasks(list)
                            .build());
        }
        return ResponseAdapter.notFound("TaskList with id:" +
                changeTasksPositionsRequest.getTaskListId() + " not exist");
    }

    public ResponseEntity assignTask(AssignTaskRequestDTO assignTaskRequestDTO) {
        Optional<Task> optionalTask = taskRepository.findById(assignTaskRequestDTO.getTaskId());
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            TaskList taskList = task.getTaskList();
            Board board = taskList.getBoard();
            Optional<Member> optionalMember = userRepository.findById(assignTaskRequestDTO.getAssignedUserId());
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                if (isOwner(board, member) || isMemberOfBoard(board, member)) {
                    task.setContractor(member);
                    taskRepository.save(task);
                    return ResponseAdapter.ok();
                }
                return ResponseAdapter.forbidden("User has not privileges to this board");
            }
            return ResponseAdapter.notFound("User with id: " + assignTaskRequestDTO.getAssignedUserId() + " not exist");
        }
        return ResponseAdapter.notFound("Task with id: " + assignTaskRequestDTO.getTaskId() + " not exist");
    }

    public ResponseEntity moveTask(MoveTaskRequestDTO moveTaskRequestDTO) {
        Optional<Task> optionalTask = taskRepository.findById(moveTaskRequestDTO.getTaskId());
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            TaskList taskList = task.getTaskList();
            Optional<TaskList> optionalTaskList = taskListRepository.findById(moveTaskRequestDTO.getTaskListId());
            if (optionalTaskList.isPresent()) {
                TaskList toMove = optionalTaskList.get();
                task.setTaskList(toMove);
                taskRepository.save(task);
                return ResponseAdapter.ok();
            }
            return ResponseAdapter.notFound("Not found taskList with id: " + moveTaskRequestDTO.getTaskListId());
        }
        return ResponseAdapter.notFound("Task with id: " + moveTaskRequestDTO.getTaskId() + " not exist");
    }


    public ResponseEntity getTask(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();

            List<AttachmentDto> attachments = new ArrayList<>();
            task.getAttachments().forEach(attachment -> attachments.add(AttachmentDto.builder()
                    .attachmentId(attachment.getAttachmentId())
                    .name(attachment.getName())
                    .build()));


            return ResponseAdapter.ok(TaskDto.builder()
                    .taskId(task.getTaskId())
                    .description(task.getDescription())
                    .date(dateToString(task.getDate()))
                    .reporterId(task.getReporter().getMemberId())
                    .contractorId(createEmptyContractor(task.getContractor()))
                    .taskListId(task.getTaskList().getTaskListId())
                    .comments(task.getComments().stream().map(Comment::getCommentId).collect(Collectors.toList()))
                    .attachments(attachments)
                    .build());
        }
        return ResponseAdapter.notFound("Task not exist");
    }

    public static boolean isOwner(Board board, Member member) {
        return board.getOwner().equals(member);
    }

    public static boolean isMemberOfBoard(Board board, Member member) {
        return board.getMembers().stream()
                .anyMatch(m -> m.getUsername().equals(member.getUsername()));
    }

    public static String dateToString(LocalDateTime date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return date.toString();
    }

    private Long createEmptyContractor(Member contractor) {
        if (contractor != null) {
            return contractor.getMemberId();
        }
        return (long) -1;
    }

}
