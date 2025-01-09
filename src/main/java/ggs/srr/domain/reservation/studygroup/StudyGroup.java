package ggs.srr.domain.reservation.studygroup;

import ggs.srr.domain.reservation.usergroup.UserGroup;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "groups")
public class StudyGroup {

    @Id
    @GeneratedValue
    @Column(name = "study_group_id")
    private Long id;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroups = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public StudyGroup() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

}
