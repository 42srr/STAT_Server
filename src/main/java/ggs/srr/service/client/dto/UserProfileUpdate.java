package ggs.srr.service.client.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class UserProfileUpdate {
    private final double level;
    private final int collection_point;
    private final int wallet;
    private final List<ProjectDetailInfo> in_progress_projects;
    private final List<ProjectDetailInfo> finished_projects;

    public UserProfileUpdate(double level, int collection_point, int wallet,
                             List<ProjectDetailInfo> in_progress_projects,
                             List<ProjectDetailInfo> finished_projects) {
        this.level = level;
        this.collection_point = collection_point;
        this.wallet = wallet;
        this.in_progress_projects = in_progress_projects;
        this.finished_projects = finished_projects;
    }
}
