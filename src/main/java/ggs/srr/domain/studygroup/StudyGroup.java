package ggs.srr.domain.studygroup;

import ggs.srr.domain.userstudygroup.UserStudyGroup;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "study_group")
public class StudyGroup {

    @Id
    @GeneratedValue
    @Column(name = "study_group_id")
    private Long id;

    @OneToMany(mappedBy = "studyGroup")
    private List<UserStudyGroup> userStudyGroups = new ArrayList<>();

    private String groupName;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    protected StudyGroup() {
    }

    public StudyGroup(String groupName) {
        this.groupName = groupName;
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

}
