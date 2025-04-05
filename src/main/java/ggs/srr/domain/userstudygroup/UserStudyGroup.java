package ggs.srr.domain.userstudygroup;

import ggs.srr.domain.studygroup.StudyGroup;
import ggs.srr.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserStudyGroup {

    @Id @GeneratedValue
    @Column(name = "user_study_group")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void registerGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
        studyGroup.getUserStudyGroups().add(this);
    }

    public void registerUser(User user) {
        this.user = user;
        user.getUserStudyGroups().add(this);
    }
}
