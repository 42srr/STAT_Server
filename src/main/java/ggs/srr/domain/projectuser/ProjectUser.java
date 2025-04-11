package ggs.srr.domain.projectuser;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProjectUserStatus status;

    private Integer finalMark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    private ProjectUser(ProjectUserStatus status, int finalMark, User user, Project project) {
        this.status = status;
        this.finalMark = finalMark;
        this.user = user;
        this.project = project;

        user.getProjectUsers().add(this);
        project.getProjectUsers().add(this);
    }
}
