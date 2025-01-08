package ggs.srr.domain.reservation.group;

import ggs.srr.domain.reservation.usergroup.UserGroup;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Group {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroups = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public Group() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

}
