package pl.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.trello.entity.TaskList;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
}
