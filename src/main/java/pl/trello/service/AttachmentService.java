package pl.trello.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.trello.adapters.ResponseAdapter;
import pl.trello.dto.request.AddAttachmentDto;
import pl.trello.dto.response.valid.AttachmentDto;
import pl.trello.entity.Attachment;
import pl.trello.entity.Board;
import pl.trello.entity.Comment;
import pl.trello.entity.Member;
import pl.trello.entity.Task;
import pl.trello.repository.AttachmentRepository;
import pl.trello.repository.BoardRepository;
import pl.trello.repository.CommentRepository;
import pl.trello.repository.MemberRepository;
import pl.trello.repository.TaskListRepository;
import pl.trello.repository.TaskRepository;
import pl.trello.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final TaskListRepository taskListRepository;
    private final BoardRepository boardRepository;

    public AttachmentService(AttachmentRepository attachmentRepository, UserRepository userRepository, MemberRepository memberRepository, TaskRepository taskRepository, CommentRepository commentRepository, TaskListRepository taskListRepository, BoardRepository boardRepository) {
        this.attachmentRepository = attachmentRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.taskListRepository = taskListRepository;
        this.boardRepository = boardRepository;
    }


    public ResponseEntity addAttachmentToTask(AddAttachmentDto addAttachmentDto) {
        Optional<Member> memberOptional = userRepository.findByUsername(addAttachmentDto.getUsername());
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Optional<Task> optionalTask = taskRepository.findById(addAttachmentDto.getId());
            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                Board board = task.getTaskList().getBoard();
                Set<Member> membersSet = new HashSet<>(board.getMembers());
                if (membersSet.contains(member) || board.getOwner().equals(member)) {
                    List<Attachment> attachments = task.getAttachments();
                    if(attachments == null){
                        attachments = new ArrayList<>();
                        task.setAttachments(attachments);
                    }
                    Attachment attachment = attachmentRepository.save(Attachment.builder()
                            .name(addAttachmentDto.getName())
                            .content(addAttachmentDto.getContent())
                            .build());
                    attachments.add(attachment);
                    task.setAttachments(attachments);
                    taskRepository.save(task);

                    return ResponseAdapter.ok(AttachmentDto.builder()
                            .attachmentId(attachment.getAttachmentId())
                            .name(attachment.getName())
                            .build());
                }
                return ResponseAdapter.forbidden("User have not permission to this board");
            }
            return ResponseAdapter.notFound("Task not exist");
        }
        return ResponseAdapter.notFound("User not exist");
    }

    public ResponseEntity addAttachmentToComment(AddAttachmentDto addAttachmentDto) {
        Optional<Member> memberOptional = userRepository.findByUsername(addAttachmentDto.getUsername());
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Optional<Comment> optionalComment = commentRepository.findById(addAttachmentDto.getId());
            if (optionalComment.isPresent()) {
                Comment comment = optionalComment.get();
                Board board = comment.getTask().getTaskList().getBoard();
                Set<Member> membersSet = new HashSet<>(board.getMembers());
                if (membersSet.contains(member) || board.getOwner().equals(member)) {
                    List<Attachment> attachments = comment.getAttachments();
                    if(attachments == null){
                        attachments = new ArrayList<>();
                    }
                    Attachment attachment = attachmentRepository.save(Attachment.builder()
                            .name(addAttachmentDto.getName())
                            .content(addAttachmentDto.getContent())
                            .build());
                    attachments.add(attachment);
                    comment.setAttachments(attachments);
                    commentRepository.save(comment);
                    return ResponseAdapter.ok(AttachmentDto.builder()
                            .attachmentId(attachment.getAttachmentId())
                            .name(attachment.getName())
                            .build()
                    );
                }
                return ResponseAdapter.forbidden("User have not permission to this board");
            }
            return ResponseAdapter.notFound("Comment not exist");
        }
        return ResponseAdapter.notFound("User not exist");
    }


    public ResponseEntity getAttachment(Long attachmentId) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(attachmentId);
        if (optionalAttachment.isPresent()) {
            return ResponseAdapter.ok(optionalAttachment.get());
        }
        return pl.trello.ResponseAdapter.notFound("Attachment not exist");
    }
}
