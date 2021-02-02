package woo.young.tobyproject.domain;

import lombok.Getter;

@Getter
public enum Level {
    GOLD(null),  SILVER(Level.GOLD), BASIC(Level.SILVER);

    private final Level next;

    Level(Level next) {
        this.next = next;
    }

}

