package pl.trello.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DeleteAttachmentRequestDTO {

    private Long attachmentId;

    private String login;
}
