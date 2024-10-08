package ggs.srr.domain.project;

import ggs.srr.domain.projectuser.ProjectUser;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Project {

    @Id @GeneratedValue
    private long id;


    private String name;

    @OneToMany(mappedBy = "project")
    private List<ProjectUser> projectUsers = new ArrayList<>();

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }
}
