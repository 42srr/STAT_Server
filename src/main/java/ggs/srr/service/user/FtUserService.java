package ggs.srr.service.user;

import ggs.srr.domain.FtUser;
import ggs.srr.repository.user.FtUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class FtUserService {

    private final FtUserRepository repository;

    public Long save(FtUser user){
        return repository.save(user);
    }

    public List<FtUser> findAll(){
        return repository.findAll();
    }

    public FtUser findByIntraId(String intraId){
        return repository.findByIntraId(intraId);
    }

    public Map<Integer, Integer> levelDistributionInfo() {
        Map<Integer, Integer> levelInfo = new HashMap<>();
        List<FtUser> ftUsers = repository.findAll();
        Collections.sort(ftUsers, new Comparator<FtUser>() {
            @Override
            public int compare(FtUser o1, FtUser o2) {
                return (int) o2.getLevel() - (int) o1.getLevel();
            }
        });
        for (FtUser ftUser : ftUsers) {
            if (ftUser.getLevel() == 0.00)
                continue;
            int levelKey = (int) ftUser.getLevel();
            if (levelInfo.containsKey(levelKey)) {
                int beforeData = levelInfo.get(levelKey);
                levelInfo.put(levelKey, ++beforeData);
            } else {
                levelInfo.put(levelKey, 1);
            }
        }
        return levelInfo;
    }
}
