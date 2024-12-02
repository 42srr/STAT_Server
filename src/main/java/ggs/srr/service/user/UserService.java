package ggs.srr.service.user;

import ggs.srr.api.controller.main.dto.LevelDto;
import ggs.srr.api.controller.user.dto.UserResponse;
import ggs.srr.domain.user.FtUser;
import ggs.srr.repository.user.UserRepository;
import java.util.ArrayList;
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

    public LevelDto getLevelInfo() {

        LevelDto levelDto = new LevelDto();
        List<FtUser> users = userRepository.findAll();
        for (FtUser user : users) {
            int level = (int) Math.floor(user.getLevel());
            int levelValue = getLevelValue(levelDto, level);
            setLevelValue(levelDto, level, levelValue + 1);
        }
        return levelDto;
    }

    private int getLevelValue(LevelDto levelDto, int level) {
        switch (level) {
            case 0: return levelDto.getLevel_0();
            case 1: return levelDto.getLevel_1();
            case 2: return levelDto.getLevel_2();
            case 3: return levelDto.getLevel_3();
            case 4: return levelDto.getLevel_4();
            case 5: return levelDto.getLevel_5();
            case 6: return levelDto.getLevel_6();
            case 7: return levelDto.getLevel_7();
            case 8: return levelDto.getLevel_8();
            case 9: return levelDto.getLevel_9();
            case 10: return levelDto.getLevel_10();
            case 11: return levelDto.getLevel_11();
            case 12: return levelDto.getLevel_12();
            case 13: return levelDto.getLevel_13();
            case 14: return levelDto.getLevel_14();
            case 15: return levelDto.getLevel_15();
            case 16: return levelDto.getLevel_16();
            case 17: return levelDto.getLevel_17();
            case 18: return levelDto.getLevel_18();
            case 19: return levelDto.getLevel_19();
            case 20: return levelDto.getLevel_20();
            case 21: return levelDto.getLevel_21();
            default: return 0; // 유효하지 않은 경우
        }
    }

    private void setLevelValue(LevelDto levelDto, int level, int value) {
        switch (level) {
            case 0: levelDto.setLevel_0(value); break;
            case 1: levelDto.setLevel_1(value); break;
            case 2: levelDto.setLevel_2(value); break;
            case 3: levelDto.setLevel_3(value); break;
            case 4: levelDto.setLevel_4(value); break;
            case 5: levelDto.setLevel_5(value); break;
            case 6: levelDto.setLevel_6(value); break;
            case 7: levelDto.setLevel_7(value); break;
            case 8: levelDto.setLevel_8(value); break;
            case 9: levelDto.setLevel_9(value); break;
            case 10: levelDto.setLevel_10(value); break;
            case 11: levelDto.setLevel_11(value); break;
            case 12: levelDto.setLevel_12(value); break;
            case 13: levelDto.setLevel_13(value); break;
            case 14: levelDto.setLevel_14(value); break;
            case 15: levelDto.setLevel_15(value); break;
            case 16: levelDto.setLevel_16(value); break;
            case 17: levelDto.setLevel_17(value); break;
            case 18: levelDto.setLevel_18(value); break;
            case 19: levelDto.setLevel_19(value); break;
            case 20: levelDto.setLevel_20(value); break;
            case 21: levelDto.setLevel_21(value); break;
        }
    }
}
