package pl.trello.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.trello.ResponseAdapter;
import pl.trello.dto.request.AssignTaskRequestDTO;
import pl.trello.dto.request.EditTaskRequestDTO;
import pl.trello.dto.request.MoveTaskRequestDTO;
import pl.trello.dto.response.valid.AttachmentDto;
import pl.trello.dto.response.valid.TaskDto;
import pl.trello.entity.Board;
import pl.trello.entity.Comment;
import pl.trello.entity.Member;
import pl.trello.entity.Task;
import pl.trello.entity.TaskList;
import pl.trello.repository.TaskListRepository;
import pl.trello.repository.TaskRepository;
import pl.trello.repository.UserRepository;
import pl.trello.request.AddTaskRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, TaskListRepository taskListRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity addTask(AddTaskRequest addTaskRequest) {
        Optional<Member> optionalMember = userRepository.findByUsername(addTaskRequest.getUsername());
        if (optionalMember.isPresent()) {
            Optional<TaskList> optionalTaskList = taskListRepository.findById(addTaskRequest.getTaskListId());
            if (optionalTaskList.isPresent()) {
                Board board = optionalTaskList.get().getBoard();
                if (isOwner(board, optionalMember.get()) || isMemberOfBoard(board, optionalMember.get())) {
                    Task task = Task.builder()
                            .taskList(optionalTaskList.get())
                            .comments(new ArrayList<>())
                            .description(addTaskRequest.getDescription())
                            .reporter(optionalMember.get())
                            .build();
                    taskRepository.save(task);
                    return ResponseAdapter.ok();
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
                    taskRepository.save(task);
                    return ResponseAdapter.ok();
                }
                return ResponseAdapter.forbidden("User has not privileges to this board");
            }
            return ResponseAdapter.notFound("User with username: " + editTaskRequestDTO.getUsername() + " not exist");
        }
        return ResponseAdapter.notFound("Task with id: " + editTaskRequestDTO.getTaskId() + " not exist");

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
            Long contractorId = -1L;
            if(task.getContractor() != null){
                contractorId =  task.getContractor().getMemberId();
            }

            return ResponseAdapter.ok(TaskDto.builder()
                    .taskId(task.getTaskId())
                    .description(task.getDescription())
                    .reporterId(task.getReporter().getMemberId())
                    .contractorId(contractorId)
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


}
