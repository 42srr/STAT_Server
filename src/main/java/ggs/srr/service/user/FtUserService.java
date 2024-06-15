package ggs.srr.service.user;

import ggs.srr.domain.FtUser;
import ggs.srr.repository.user.FtUserRepository;
import ggs.srr.service.user.dto.RankingEvalPointDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map;
import java.util.Comparator; 
import java.util.ArrayList;

@Slf4j
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

    public List<RankingEvalPointDto> rankingEvalPoint() {
        List<FtUser> ftUsers = repository.findAll();
        List<RankingEvalPointDto> result = new ArrayList<>();
        Collections.sort(ftUsers, new Comparator<FtUser>() {
            @Override
            public int compare(FtUser o1, FtUser o2) { return o2.getCorrection_point() - o1.getCorrection_point(); }
        });
        ftUsers.forEach(ftUser -> {
            log.info("user" + ftUser);
            if (ftUser.getLevel() != 0.00)
                result.add(new RankingEvalPointDto(ftUser.getImage(), ftUser.getIntraId(), ftUser.getCorrection_point()));
        });
        return result;
    }
}
