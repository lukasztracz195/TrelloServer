package pl.trello.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.trello.dto.request.AddBoardRequestDTO;
import pl.trello.dto.request.AddCommentRequestDTO;
import pl.trello.request.AddBoardRequest;
import pl.trello.request.AddCommentRequest;
import pl.trello.service.CommentService;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static pl.trello.controller.CommentController.COMMENTS_PATH;

@RestController
@RequestMapping(COMMENTS_PATH)
public class CommentController {
    static final String COMMENTS_PATH = "/comments";

    private static final String COMMENT_ID = "commentId";
    private static final String COMMENT_ID_VARIABLE = "{" + COMMENT_ID + "}";

    private static final String TASK_ID = "taskId";
    private static final String TASK_ID_VARIABLE = "{" + TASK_ID + "}";

    private static final String ADD_PATH = TASK_ID_VARIABLE;
    private static final String EDIT_PATH = "/edit/" + COMMENT_ID_VARIABLE;
    private static final String DELETE_PATH = "/delete";



    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(
            value = ADD_PATH,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity addBoard(@PathVariable(TASK_ID) Long taskId, @RequestBody AddCommentRequest addCommentRequest,
                                   Principal principal) {
        return commentService.addComment(AddCommentRequestDTO.builder()
        .taskId(taskId)
        .username(principal.getName())
        .content(addCommentRequest.getContent())
        .build());
    }

//    @PostMapping(
//            value = EDIT_PATH,
//            consumes = APPLICATION_JSON_UTF8_VALUE,
//            produces = APPLICATION_JSON_UTF8_VALUE
//    )
//    public ResponseEntity editBoard(@PathVariable(COMMENT_ID), Long commentId, @RequestBody)
}
