package pl.trello.dto.response.valid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.checkerframework.checker.signature.qual.BinaryNameInUnnamedPackage;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class TaskDto {

    private Long taskId;

    private Long taskListId;

    private String description;

    private List<Long> comments;

    private List<AttachmentDto> attachments;

    private Long reporterId;

    private Long contractorId;
}
