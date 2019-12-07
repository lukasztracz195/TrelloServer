package pl.trello.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.trello.dto.request.GetBoardRequestDTO;
import pl.trello.dto.response.invalid.InvalidResponse;
import pl.trello.dto.response.valid.AddBoardResponseDTO;
import pl.trello.dto.response.valid.AddTaskListResponseDTO;
import pl.trello.dto.response.valid.BoardResponseDTO;
import pl.trello.dto.response.valid.ChangeBoardNameResponseDTO;
import pl.trello.dto.response.valid.GetBoardsResponseDTO;
import pl.trello.entity.Board;
import pl.trello.entity.Member;
import pl.trello.entity.TaskList;
import pl.trello.repository.BoardRepository;
import pl.trello.repository.UserRepository;
import pl.trello.request.AddBoardRequest;
import pl.trello.request.ChangeBoardNameRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository,
                        UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<AddBoardResponseDTO> addBoard(AddBoardRequest addBoardRequest) {
        //TODO validate

        Member owner = userRepository.findByUsername(addBoardRequest.getLogin()).get();
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


        Optional<Member> optionalMember = userRepository.findByUsername(changeBoardNameRequest.getUsername());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            long memberId = member.getMemberId();
            List<Board> boards = boardRepository.findListByOwner(member.getMemberId());
            if (boards.stream().anyMatch(b -> b.getOwner().equals(member))) {
                Board board = boardRepository.findById(changeBoardNameRequest.getBoardId()).get();
                board.setName(changeBoardNameRequest.getBoardName());
                board = boardRepository.save(board);

                return ResponseEntity.ok(ChangeBoardNameResponseDTO.builder()
                        .boardId(board.getBoardId())
                        .ownerId(board.getOwner().getMemberId())
                        .members(board.getMembers())
                        .columns(board.getTaskLists())
                        .name(board.getName())
                        .build());
            }
            return ResponseEntity.of(InvalidResponse.of("Username is not owner this board"));
        }
        return ResponseEntity.of(InvalidResponse.of("User with username: " + changeBoardNameRequest.getUsername() + " not exist"));
    }

    public ResponseEntity getBoards(String username) {
        Optional<Member> optionalMember = userRepository.findByUsername(username);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            List<Board> boards = findAllConnectedByMemberId(member.getMemberId());
            List<BoardResponseDTO> boardResponseDTOS = new ArrayList<>();
            for (Board board : boards) {
                boardResponseDTOS.add(BoardResponseDTO.builder()
                        .name(board.getName())
                        .owner(board.getOwner().getUsername())
                        .membersNames(board.getMembers().stream()
                                .map(f -> member.getUsername())
                                .map(m -> m)
                                .collect(Collectors.toList()))
                        .taskLists(prepareTaskListResponse(board))
                        .build());
            }

            return ResponseEntity.ok(GetBoardsResponseDTO.builder()
                    .boards(boardResponseDTOS)
                    .build());
        }
        return ResponseEntity.of(InvalidResponse.of("User with username: " + username + " not exist"));
    }

    public ResponseEntity getBoard(GetBoardRequestDTO getBoardRequestDTO, Long boardId) {
        Optional<Member> optionalMember = userRepository.findByUsername(getBoardRequestDTO.getUsername());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            List<Board> boards = findAllConnectedByMemberId(member.getMemberId());
            if (boards.stream().anyMatch(f -> f.getBoardId().equals(boardId))) {
                Board board = boardRepository.findById(boardId).get();
                return ResponseEntity.ok(BoardResponseDTO.builder()
                        .name(board.getName())
                        .boardId(board.getBoardId())
                        .owner(board.getOwner().getUsername())
                        .membersNames(board.getMembers().stream()
                                .map(Member::getUsername)
                                .collect(Collectors.toList()))
                        .taskLists(prepareTaskListResponse(board))
                        .build());
            }
            return ResponseEntity.of(InvalidResponse.of("User have not privillages to this board"));
        }
        return ResponseEntity.of(InvalidResponse.of("User with username: " + getBoardRequestDTO.getUsername() + " not exist"));
    }

    private List<Board> findAllConnectedByMemberId(long memberId) {
        Member member = userRepository.getOne(memberId);
        List<Board> boardsWhichMemberIsOwner = boardRepository.findListByOwner(member.getMemberId());
        List<Board> boardsWhichMemberIsMember = new ArrayList<>();
        List<Board> allBoards = boardRepository.findAll();
        for (Board board : allBoards) {
            if (board.getMembers().contains(member)) {
                boardsWhichMemberIsMember.add(board);
            }
        }
        while (!boardsWhichMemberIsOwner.isEmpty()) {
            boardsWhichMemberIsMember.add(boardsWhichMemberIsOwner.get(boardsWhichMemberIsOwner.size() - 1));
            boardsWhichMemberIsOwner.remove(boardsWhichMemberIsOwner.size() - 1);
        }
        return boardsWhichMemberIsMember;
    }

    private List<AddTaskListResponseDTO> prepareTaskListResponse(Board board) {
        List<AddTaskListResponseDTO> taskLists = new ArrayList<>();
        for (TaskList taskList : board.getTaskLists()) {
            taskLists.add(AddTaskListResponseDTO.builder()
                    .name(taskList.getName())
                    .boardId(taskList.getBoard().getBoardId())
                    .position(taskList.getPosition())
                    .taskListId(taskList.getTaskListId())
                    .build());
        }
        return taskLists;
    }
}
