package ggs.srr.domain.user;

import lombok.Getter;

@Getter
public enum Role {
    CADET("cardet"),
    ADMIN("admin"),
    BOCAL("bocal");

    private final String text;

    Role(String text) {
        this.text = text;
    }

    public static boolean isAdmin(String role) {
        return ADMIN.getText().equals(role);
    }
}
