package ggs.srr.domain.user;

import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.userstudygroup.UserStudyGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ftServerId;

    @Column(unique = true)
    private String intraId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private int wallet;
    private int collectionPoint;
    private double level;
    private String image;

    private String jwtRefreshToken;
    private String oAuth2AccessToken;
    private String oAuth2RefreshToken;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<ProjectUser> projectUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserStudyGroup> userStudyGroups = new ArrayList<>();

    @Builder
    private User(Long ftServerId, String intraId, Role role, int wallet, int collectionPoint, double level, String image, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.ftServerId = ftServerId;
        this.intraId = intraId;
        this.role = role;
        this.wallet = wallet;
        this.collectionPoint = collectionPoint;
        this.level = level;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateAuthenticationServerToken(String oAuth2AccessToken, String oAuth2RefreshToken) {
        this.oAuth2AccessToken = oAuth2AccessToken;
        this.oAuth2RefreshToken = oAuth2RefreshToken;
    }

    public void updateRefreshToken(String jwtRefreshToken) {
        this.jwtRefreshToken = jwtRefreshToken;
    }

    public void initializeDateTime(LocalDateTime dateTime) {
        this.createdAt = dateTime;
        this.updatedAt = dateTime;
    }
}
