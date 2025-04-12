package ggs.srr.domain.projectuser;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ProjectUserStatus {
    FINISHED("finished"),
    IN_PROGRESS("in_progress"),
    NOT_REGISTERED("not_registered"),
    DEFAULT("default")
    ;

    private final String text;

    ProjectUserStatus(String text) {
        this.text = text;
    }

    public static ProjectUserStatus getByText(String text) {

        if (text == null) {
            return NOT_REGISTERED;
        }

        return Arrays.stream(values())
                .filter(status -> status.getText().equals(text))
                .findAny()
                .orElse(DEFAULT);
    }

    public String getText() {
        return this.text;
    }

}
