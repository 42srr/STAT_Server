package ggs.srr.domain.reservation.studygroup;

import ggs.srr.domain.reservation.usergroup.UserStudyGroup;
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

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public StudyGroup() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

}
