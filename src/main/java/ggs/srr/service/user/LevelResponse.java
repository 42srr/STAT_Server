package ggs.srr.service.user;

import lombok.Data;

@Data
public class LevelResponse {
    private int level;
    private int count;

    public LevelResponse(int level, int count) {
        this.level = level;
        this.count = count;
    }
}
