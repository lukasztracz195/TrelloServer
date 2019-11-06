package pl.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.trello.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
