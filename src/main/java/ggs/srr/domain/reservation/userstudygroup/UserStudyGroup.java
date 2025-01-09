package ggs.srr.domain.reservation.userstudygroup;

import ggs.srr.domain.reservation.studygroup.StudyGroup;
import ggs.srr.domain.user.FtUser;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserStudyGroup {

    @Id @GeneratedValue
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private FtUser user;

    public void registerGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
        studyGroup.getUserStudyGroups().add(this);
    }

    public void registerUser(FtUser user) {
        this.user = user;
        user.getUserStudyGroups().add(this);
    }
}
