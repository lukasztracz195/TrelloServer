package pl.trello.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AddCommentRequest {

    private String content;
}
