package ggs.srr.domain.projectuser;

import lombok.Getter;

@Getter
public enum ProjectUserStatus {
    FINISHED("finished"),
    IN_PROGRESS("in_progress"),
    DEFAULT("defualt")
    ;

    private final String text;

    ProjectUserStatus(String text) {
        this.text = text;
    }

    public static ProjectUserStatus getStatus(String status) {
        ProjectUserStatus[] values = values();
        for (ProjectUserStatus value : values) {
            if (value.text.equals(status)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
