package ggs.srr.service.user;

import ggs.srr.controller.user.dto.UserDto;
import ggs.srr.domain.user.FtUser;
import ggs.srr.repository.user.UserRepository;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        findUser.setJwtToken(accessToken, refreshToken);
    }

    public FtUser findById(long id) {
        return userRepository.findById(id);
    }

    public Optional<FtUser> findByIntraId(String intraId){
        return userRepository.findByIntraId(intraId);
    }

    public List<UserDto> findAll(){
        List<UserDto> users = new ArrayList<>();
        List<FtUser> findUsers = userRepository.findAll();

        findUsers.forEach((ftUser) -> users.add(new UserDto(ftUser)));
        return users;
    }

    public Map<Integer, Integer> getLevelInfo() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 21; i++) {
            map.put(i, 0);
        }
        List<FtUser> users = userRepository.findAll();
        for (FtUser user : users) {
            Double level = user.getLevel();
            if (level == 0.00)
                continue;
            Integer levelInfo = (int) Math.floor(level);
            map.put(levelInfo, map.get(levelInfo) + 1);
        }
        return map;
    }
}
