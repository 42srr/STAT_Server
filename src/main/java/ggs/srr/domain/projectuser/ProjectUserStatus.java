package ggs.srr.domain.projectuser;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ProjectUserStatus {
    FINISHED("finished"),
    IN_PROGRESS("in_progress"),
    DEFAULT("default")
    ;

    private final String text;

    ProjectUserStatus(String text) {
        this.text = text;
    }

    public static ProjectUserStatus getByText(String text) {
        return Arrays.stream(values())
                .filter(status -> status.getText().equals(text))
                .findAny()
                .orElse(DEFAULT);
    }

    public String getText() {
        return this.text;
    }

}
