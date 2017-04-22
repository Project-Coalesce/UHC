package com.coalesce.uhc.users;

import lombok.Getter;

import static com.coalesce.uhc.utilities.Statics.colour;

public enum Participation {
    SPECTATOR("&c[S] "), // Assigned to everyone in spectator mode by start
    PARTICIPATOR("&f"), // Assigned to everyone in survival/adventure mode by start
    ADMIN("&a[*] "); // Assigned to everyone in creative mode by start. Inherits PARTICIPATOR.

    @Getter private final String prefix;

    Participation(String prefix) {
        this.prefix = prefix;
    }

    public String getMessage() {
        return colour('&', prefix + "&r: %s");
    }
}
