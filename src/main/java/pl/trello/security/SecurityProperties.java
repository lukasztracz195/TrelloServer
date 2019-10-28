package pl.trello.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecurityProperties {

    private final String secret;

    private final int tokenDuration;

    public SecurityProperties(@Value("${jwt-secret}") String secret,
                              @Value("${token-duration}") int tokenDuration) {
        this.secret = secret;
        this.tokenDuration = tokenDuration;
    }
}
