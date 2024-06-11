package ggs.srr.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.mapping.Join;

@Entity
@Getter
public class ProjectUser {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private FtUser ftUser;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String status;

    public void addProjectAndUser(FtUser user, Project project, String status){
        this.ftUser = user;
        user.getProjects().add(this);
        this.project = project;
        project.getUsers().add(this);
        this.status = status;
    }

}
