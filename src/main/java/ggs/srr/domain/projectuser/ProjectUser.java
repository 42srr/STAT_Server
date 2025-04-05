package ggs.srr.domain.projectuser;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class ProjectUser {

    @Id
    @GeneratedValue
    private long id;

    private ProjectUserStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;


    public void initUser(User user) {
        this.user = user;
        user.getProjectUsers().add(this);
    }

    public void initProject(Project project) {
        this.project = project;
        project.getProjectUsers().add(this);
    }

    public void initStatus(String status) {
        this.status = ProjectUserStatus.getStatus(status);
    }
}
