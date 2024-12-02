package ggs.srr.service.ranking;

import ggs.srr.api.controller.user.dto.RankingEvalPointDto;
import ggs.srr.api.controller.user.dto.RankingWalletDto;
import ggs.srr.domain.user.FtUser;
import ggs.srr.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RankingService {

    private final UserRepository userRepository;

    public List<RankingEvalPointDto> rankingEvalPoint() {
        List<FtUser> ftUsers = userRepository.findAll();
        List<RankingEvalPointDto> result = new ArrayList<>();

        Collections.sort(ftUsers, new Comparator<FtUser>() {
            @Override
            public int compare(FtUser o1, FtUser o2) {
                return o2.getCollectionPoint() - o1.getCollectionPoint();
            }
        });

        ftUsers.forEach(ftUser -> {
            if (ftUser.getLevel() != 0.00)
                result.add(new RankingEvalPointDto(ftUser.getImage(), ftUser.getIntraId(), ftUser.getCollectionPoint()));
        });

        return result;
    }

    public List<RankingWalletDto> rankingWallet() {
        List<FtUser> ftUsers = userRepository.findAll();
        List<RankingWalletDto> result = new ArrayList<>();

        Collections.sort(ftUsers, new Comparator<FtUser>() {
            @Override
            public int compare(FtUser o1, FtUser o2) { return o2.getWallet() - o1.getWallet(); }
        });

        ftUsers.forEach(ftUser -> {
            if (ftUser.getLevel() != 0.00) {
                result.add(new RankingWalletDto(ftUser.getImage(), ftUser.getIntraId(), ftUser.getWallet()));
            }
        });

        return result;
    }
}
