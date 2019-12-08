package pl.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.trello.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
