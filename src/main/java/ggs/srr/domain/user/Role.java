package ggs.srr.domain.user;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    CADET("cardet"),
    ADMIN("admin"),
    BOCAL("bocal");

    private final String text;

    Role(String text) {
        this.text = text;
    }

    public static boolean isAdmin(Role role) {
        return role == ADMIN;
    }

    public static Role getRoleBy(String text) {
        return Arrays.stream(values())
                .filter(r -> r.getText().equals(text))
                .findAny()
                .orElse(CADET);
    }
}
