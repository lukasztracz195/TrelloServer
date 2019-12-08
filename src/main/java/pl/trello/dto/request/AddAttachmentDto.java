package pl.trello.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AddAttachmentDto {

    private Long id;

    private String name;

    private String content;

    private String username;
}
