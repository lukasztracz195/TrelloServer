package pl.trello.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.trello.ResponseAdapter;
import pl.trello.dto.request.AddCommentRequestDTO;
import pl.trello.dto.request.EditCommentDto;
import pl.trello.dto.response.valid.AttachmentDto;
import pl.trello.dto.response.valid.CommentDto;
import pl.trello.entity.Board;
import pl.trello.entity.Comment;
import pl.trello.entity.Member;
import pl.trello.entity.Task;
import pl.trello.repository.CommentRepository;
import pl.trello.repository.TaskRepository;
import pl.trello.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    public ResponseEntity addComment(AddCommentRequestDTO addCommentRequestDTO) {
        Optional<Member> optionalMember = userRepository.findByUsername(addCommentRequestDTO.getUsername());
        if (optionalMember.isPresent()) {
            Optional<Task> optionalTask = taskRepository.findById(addCommentRequestDTO.getTaskId());
            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                Board board = task.getTaskList().getBoard();
                Member member = optionalMember.get();
                if (TaskService.isOwner(board, member) || TaskService.isMemberOfBoard(board, member)) {
                    Comment comment = Comment.builder()
                            .content(addCommentRequestDTO.getContent())
                            .createdAt(LocalDateTime.now())
                            .owner(member)
                            .task(task)
                            .build();
                    commentRepository.save(comment);
                    return ResponseAdapter.ok();
                }
                return ResponseAdapter.forbidden("User have not privileges to this board");
            }
            return ResponseAdapter.notFound("Not exist Task with id: " + addCommentRequestDTO.getTaskId());
        }
        return ResponseAdapter.notFound("Not exist User with name: " + addCommentRequestDTO.getUsername());
    }

    public ResponseEntity editComment(EditCommentDto editCommentDto) {
        if(editCommentDto.getCommentId() == null){
            return ResponseAdapter.badRequest();
        }
        Optional<Member> optionalMember = userRepository.findByUsername(editCommentDto.getUsername());
        if (optionalMember.isPresent()) {
            Optional<Comment> optionalComment = commentRepository.findById(editCommentDto.getCommentId());
            if (optionalComment.isPresent()) {
                Comment comment = optionalComment.get();
                comment.setContent(editCommentDto.getContent());
                commentRepository.save(comment);
                return ResponseAdapter.ok();
            }
            return ResponseAdapter.notFound("Not exist Comment with id: " + editCommentDto.getCommentId());
        }
        return ResponseAdapter.notFound("Not exist User with name: " + editCommentDto.getUsername());
    }

    public ResponseEntity getCommit(Long commentId, Principal principal) {

        Optional<Member> optionalMember = userRepository.findByUsername(principal.getName());
        if (optionalMember.isPresent()) {
            Optional<Comment> optionalComment = commentRepository.findById(commentId);
            if (optionalComment.isPresent()) {
                Comment comment = optionalComment.get();
                List<AttachmentDto> attachments = new ArrayList<>();
                comment.getAttachments().forEach(attachment -> attachments.add(AttachmentDto.builder()
                        .attachmentId(attachment.getAttachmentId())
                        .name(attachment.getName())
                        .build()));

                return ResponseAdapter.ok(CommentDto.builder()
                        .commentId(commentId)
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .ownerId(comment.getOwner().getMemberId())
                        .taskId(comment.getTask().getTaskId())
                        .attachments(attachments)
                        .build());
            }
            return ResponseAdapter.notFound("Not exist Comment with id: " + commentId);
        }
        return ResponseAdapter.notFound("Not exist User with name: " + principal.getName());
    }
}
