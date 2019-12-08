package pl.trello.dto.response.valid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AttachmentDto {

    Long attachmentId;

    String name;

}
