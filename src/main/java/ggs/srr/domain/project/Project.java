package ggs.srr.domain.project;

import ggs.srr.domain.projectuser.ProjectUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "project")
    private List<ProjectUser> projectUsers = new ArrayList<>();

    @Builder
    private Project(String name) {
        this.name = name;
    }
}
