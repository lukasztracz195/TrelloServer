package pl.trello.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.trello.dto.response.AddBoardResponseDTO;
import pl.trello.dto.response.ChangeBoardNameResponseDTO;
import pl.trello.entity.Board;
import pl.trello.entity.Member;
import pl.trello.repository.BoardRepository;
import pl.trello.repository.UserRepository;
import pl.trello.request.AddBoardRequest;
import pl.trello.request.ChangeBoardNameRequest;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository,
                        UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity addBoard(AddBoardRequest addBoardRequest) {
        //TODO validate

        Member owner = userRepository.findByLogin(addBoardRequest.getLogin()).get();
        Board board = Board.builder()
                .owner(owner)
                .name(addBoardRequest.getBoardName())
                .build();

        board = boardRepository.save(board);

        return ResponseEntity.ok(AddBoardResponseDTO.builder()
                .boardId(board.getBoardId())
                .ownerId(board.getOwner().getMemberId())
                .name(board.getName())
                .build());
    }

    public ResponseEntity changeBoardName(ChangeBoardNameRequest changeBoardNameRequest) {
        //TODO validate

        Board board = boardRepository.findById(changeBoardNameRequest.getBoardId()).get();
        board.setName(changeBoardNameRequest.getBoardName());
        board = boardRepository.save(board);

        return ResponseEntity.ok(ChangeBoardNameResponseDTO.builder()
                .boardId(board.getBoardId())
                .ownerId(board.getOwner().getMemberId())
                .members(board.getMembers())
                .columns(board.getColumns())
                .name(board.getName())
                .build());
    }
}
