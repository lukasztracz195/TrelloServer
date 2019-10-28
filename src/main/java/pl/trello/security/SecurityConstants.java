package pl.trello.security;

final class SecurityConstants {

    static final String AUTH_LOGIN_URL = "/login";

    static final String TOKEN_HEADER = "Authorization";
    static final String TOKEN_PREFIX = "Bearer ";
    static final String TOKEN_TYPE = "JWT";
    static final String TOKEN_ISSUER = "trello-secure";
    static final String TOKEN_AUDIENCE = "trelloapp";
    static final String TYPE = "type";

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}

