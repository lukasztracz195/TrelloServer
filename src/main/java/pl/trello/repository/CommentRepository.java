package pl.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.trello.entity.Comment;
import pl.trello.entity.Task;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("FROM Comment c WHERE c.attachments.size > 0")
    List<Comment> findCommentsByAttachments();
}
