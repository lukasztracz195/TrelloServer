package pl.trello.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class ChangeTaskListNameRequest {

    private String name;
}
