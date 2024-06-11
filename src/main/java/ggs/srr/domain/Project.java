package ggs.srr.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Entity
@Getter
public class Project {

    public Project() {
    }

    public Project(Long resourceId, String name) {
        this.resourceId = resourceId;
        this.name = name;
    }

    @Id @GeneratedValue
    private Long id;

    private Long resourceId;

    private String name;

    @OneToMany(mappedBy = "project")
    private List<ProjectUser> users = new ArrayList<>();



}
