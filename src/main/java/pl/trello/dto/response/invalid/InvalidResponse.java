package pl.trello.dto.response.invalid;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public class InvalidResponse {

    private String errorMessage;


    public static Optional<InvalidResponse> of(String errorMessage) {
        return  Optional.of(new InvalidResponse(errorMessage));
    }
}
