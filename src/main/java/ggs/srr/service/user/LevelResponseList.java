package ggs.srr.service.user;

import java.util.List;
import lombok.Data;

@Data
public class LevelResponseList {
    private List<LevelResponse> levelResponseList;

    public LevelResponseList(List<LevelResponse> levelResponseList) {
        this.levelResponseList = levelResponseList;
    }
}
