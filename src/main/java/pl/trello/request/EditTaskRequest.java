package pl.trello.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditTaskRequest {
    String description;
}
