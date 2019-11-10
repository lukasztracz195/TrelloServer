package pl.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.trello.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
