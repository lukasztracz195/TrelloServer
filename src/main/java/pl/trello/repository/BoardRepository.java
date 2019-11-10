package pl.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.trello.entity.Board;
import pl.trello.entity.Member;

import java.util.List;
import java.util.Set;


public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("FROM Board b WHERE b.owner.memberId = :ownerId")
    List<Board> findListByOwner(@Param("ownerId") long ownerId);

}
