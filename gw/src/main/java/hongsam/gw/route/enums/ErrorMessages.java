package hongsam.gw.route.enums;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    MISSING("missing authorization header."),
    INVALID("invalid authorization header."),
    FORBID("can not access this page.");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }
}
