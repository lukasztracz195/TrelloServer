package pl.trello.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AddAttachmentRequest {

    private String name;

    private String content;
}
