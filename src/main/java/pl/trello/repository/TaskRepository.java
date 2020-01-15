package pl.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.trello.entity.Board;
import pl.trello.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {


    @Query("FROM Task t WHERE t.attachments.size > 0")
    List<Task> findTasksByAttachments();
}
