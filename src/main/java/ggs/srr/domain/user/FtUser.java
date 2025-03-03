package ggs.srr.domain.user;

import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.userstudygroup.UserStudyGroup;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class FtUser {

    @Id
    @GeneratedValue
    private Long id;

    private long ftServerId;
    private String intraId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private int wallet;
    private int collectionPoint;
    private double level;
    private String image;

    private String jwtRefreshToken;
    private String oAuth2AccessToken;
    private String oauth2RefreshToken;

    @OneToMany(mappedBy = "user")
    private List<ProjectUser> projectUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserStudyGroup> userStudyGroups = new ArrayList<>();

    public FtUser() {
    }

    public FtUser(long ftServerId, String intraId, Role role,
                  int wallet, int collectionPoint, double level, String image) {
        this.ftServerId = ftServerId;
        this.intraId = intraId;
        this.role = role;
        this.wallet = wallet;
        this.collectionPoint = collectionPoint;
        this.level = level;
        this.image = image;
    }

    public FtUser(int ftServerId, String intraId, String role, int wallet, int correctionPoint, double level, String image) {
    }

    public void updateRefreshToken(String jwtRefreshToken) {
        this.jwtRefreshToken = jwtRefreshToken;
    }

    public void setOauth2Token(String oAuth2AccessToken, String oauth2RefreshToken) {
        this.oAuth2AccessToken = oAuth2AccessToken;
        this.oauth2RefreshToken = oauth2RefreshToken;
    }
}
