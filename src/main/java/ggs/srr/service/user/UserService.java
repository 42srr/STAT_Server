package ggs.srr.service.user;

import ggs.srr.api.controller.user.dto.UserResponse;
import ggs.srr.domain.user.FtUser;
import ggs.srr.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = false)
    public long save(FtUser user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = false)
    public void updateJWTTokens(String intraId, String accessToken, String refreshToken) {
        Optional<FtUser> byIntraId = findByIntraId(intraId);
        if (byIntraId.isEmpty())
            throw new RuntimeException("intra id is invalid");
        FtUser findUser = byIntraId.get();
        findUser.updateRefreshToken(refreshToken);
    }

    public FtUser findById(long id) {
        return userRepository.findById(id);
    }

    public Optional<FtUser> findByIntraId(String intraId){
        return userRepository.findByIntraId(intraId);
    }

    public List<UserResponse> findAll(){
        List<UserResponse> users = new ArrayList<>();
        List<FtUser> findUsers = userRepository.findAll();

        findUsers.forEach((ftUser) -> users.add(new UserResponse(ftUser)));
        return users;
    }

    public UserResponse findByIntraIdForApi(String intraId) {
        Optional<FtUser> byIntraId = userRepository.findByIntraId(intraId);
        if (byIntraId.isEmpty()) {
            throw new IllegalArgumentException("intra id 에 해당하는 사용자가 존재하지 않습니다.");
        }
        FtUser user = byIntraId.get();
        return new UserResponse(user);
    }

    public LevelResponseList getLevelInfo() {
        List<FtUser> users = userRepository.findAll();
        List<LevelResponse> levelResponseList = new ArrayList<>();
        initializeLevel(levelResponseList);

        for (FtUser user : users) {
            int level = (int) Math.floor(user.getLevel());
            updateLevel(levelResponseList, level);
        }

        return new LevelResponseList(levelResponseList);
    }

    private void initializeLevel(List<LevelResponse> levelResponseList) {
        for (int i = 0; i < 22; i++) {
            levelResponseList.add(new LevelResponse(i, 0));
        }
    }

    private void updateLevel(List<LevelResponse> levelResponseList, int level) {
        if (!isExistingLevel(levelResponseList.size(), level)) {
            addExtraLevel(levelResponseList, level);
        }
        if (isExistingLevel(levelResponseList.size(), level)) {
            increaseLevelCount(levelResponseList, level);
        }
    }

    private boolean isExistingLevel(int levelSize, int level) {
        return level >= 0 && level < levelSize;
    }

    private void addExtraLevel(List<LevelResponse> levelResponseList, int level) {
        for (int i = 0; i <= level - levelResponseList.size() + 1; i++) {
            levelResponseList.add(new LevelResponse(levelResponseList.size() + 1, 0));
        }
    }
    
    private void increaseLevelCount(List<LevelResponse> levelResponseList, int level) {
        LevelResponse existingLevel = levelResponseList.get(level);
        levelResponseList.set(level, new LevelResponse(existingLevel.getLevel(), existingLevel.getCount() + 1));
    }
}
