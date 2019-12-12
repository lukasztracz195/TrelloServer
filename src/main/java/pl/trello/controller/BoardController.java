package pl.trello.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.trello.dto.request.AddBoardRequestDTO;
import pl.trello.dto.request.ChangeBoardNameRequestDTO;
import pl.trello.dto.request.GetBoardRequestDTO;
import pl.trello.request.AddBoardRequest;
import pl.trello.request.ChangeBoardNameRequest;
import pl.trello.service.BoardService;

import java.security.Principal;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static pl.trello.controller.BoardController.BOARDS_PATH;

@RestController
@RequestMapping(BOARDS_PATH)
public class BoardController {

    static final String BOARDS_PATH = "/boards";
    static final String BOARD_ID = "boardId";
    static final String BOARD_ID_VARIABLE = "{" + BOARD_ID + "}";
    private static final String ADD_PATH = EMPTY;
    private static final String CHANGE_BOARD_NAME_PATH = "/changeName/" + BOARD_ID_VARIABLE;
    private static final String GET_BOARDS = "/getBoards";
    private static final String GET_BOARD = "/getBoard/" + BOARD_ID_VARIABLE;

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping(
            value = ADD_PATH,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity addBoard(@RequestBody AddBoardRequestDTO addBoardRequestDTO,
                                   Principal principal) {
        return boardService.addBoard(AddBoardRequest.builder()
                .boardName(addBoardRequestDTO.getName())
                .login(principal.getName())
                .build());
    }

    @PatchMapping(
            value = CHANGE_BOARD_NAME_PATH,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity changeBoardName(@PathVariable(BOARD_ID) Long boardId,
                                          @RequestBody ChangeBoardNameRequestDTO changeBoardNameRequestDTO,
                                          Principal principal) {
        return boardService.changeBoardName(ChangeBoardNameRequest.builder()
                .boardId(boardId)
                .boardName(changeBoardNameRequestDTO.getName())
                .username(principal.getName())
                .build());
    }

    @GetMapping(
            value = GET_BOARDS,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity getBoards(Principal principal) {
        return boardService.getBoards(principal.getName());
    }

    @GetMapping(
            value = GET_BOARD,
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity getBoard(@PathVariable(BOARD_ID) Long boardId, Principal principal)
                                   {
        return boardService.getBoard(GetBoardRequestDTO.builder().username(principal.getName()).build(), boardId);
    }
}
