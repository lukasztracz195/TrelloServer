package pl.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.trello.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
