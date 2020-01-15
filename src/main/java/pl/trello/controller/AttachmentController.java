package pl.trello.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.trello.dto.request.AddAttachmentRequest;
import pl.trello.dto.request.AddAttachmentDto;
import pl.trello.dto.request.DeleteAttachmentRequestDTO;
import pl.trello.service.AttachmentService;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(AttachmentController.ATTACHMENTS_PATH)
public class AttachmentController {
    static final String ATTACHMENTS_PATH = "/attachments";
    private static final String TASK_ID = "taskId";
    private static final String COMMENT_ID = "commentId";
    private static final String ATTACHMENT_ID = "attachmentId";

    private static final String ATTACHMENT_ID_PATH_VARIABLE = "{" + ATTACHMENT_ID + "}";
    private static final String COMMENT_ID_PATH_VARIABLE = "{" + COMMENT_ID + "}";
    private static final String TASK_ID_PATH_VARIABLE = "{" + TASK_ID + "}";

    private static final String ADD_PATH_TO_TASK = "/task/" + TASK_ID_PATH_VARIABLE;
    private static final String ADD_PATH_TO_COMMENT = "/comment/" + COMMENT_ID_PATH_VARIABLE;
    private static final String GET_PATH = "/get/" + ATTACHMENT_ID_PATH_VARIABLE;
    private static final String DELETE_ATTACHMENT_PATH = "/" + ATTACHMENT_ID_PATH_VARIABLE +"/delete";


    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping(
            value = ADD_PATH_TO_TASK,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity addToTask(@PathVariable(TASK_ID) Long taskId,
                                    @RequestBody AddAttachmentRequest addAttachmentRequest,
                                    Principal principal) {
        return attachmentService.addAttachmentToTask(AddAttachmentDto.builder()
                .name(addAttachmentRequest.getName())
                .content(addAttachmentRequest.getContent())
                .username(principal.getName())
                .id(taskId)
                .build());
    }

    @PostMapping(
            value = ADD_PATH_TO_COMMENT,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity addToComment(@PathVariable(COMMENT_ID) Long taskId,
                                       @RequestBody AddAttachmentRequest addAttachmentRequest,
                                       Principal principal) {
        return attachmentService.addAttachmentToComment(AddAttachmentDto.builder()
                .name(addAttachmentRequest.getName())
                .content(addAttachmentRequest.getContent())
                .username(principal.getName())
                .id(taskId)
                .build());
    }

    @GetMapping(
            value = GET_PATH,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity get(@PathVariable(ATTACHMENT_ID) Long attachmentId) {
        return attachmentService.getAttachment(attachmentId);
    }

    @DeleteMapping(
            value = DELETE_ATTACHMENT_PATH,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity delete(@PathVariable(ATTACHMENT_ID) Long attachmentId, Principal principal) {
        return attachmentService.deleteAttachment(DeleteAttachmentRequestDTO.builder()
        .attachmentId(attachmentId)
        .login(principal.getName())
        .build());
    }
}
