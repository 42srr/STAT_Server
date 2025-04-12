package ggs.srr.service.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CreateProjectServiceRequest {

    private String projectName;

    public CreateProjectServiceRequest(String projectName) {
        this.projectName = projectName;
    }
}
