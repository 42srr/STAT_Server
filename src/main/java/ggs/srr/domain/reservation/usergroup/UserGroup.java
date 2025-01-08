package ggs.srr.domain.reservation.usergroup;

import ggs.srr.domain.reservation.group.Group;
import ggs.srr.domain.user.FtUser;
import jakarta.persistence.*;

@Entity
public class UserGroup {

    @Id @GeneratedValue
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private FtUser user;

    public void registerGroup(Group group) {
        this.group = group;
        group.getUserGroups().add(this);
    }

    public void registerUser(FtUser user) {
        this.user = user;
        user.getUserGroups().add(this);
    }
}
