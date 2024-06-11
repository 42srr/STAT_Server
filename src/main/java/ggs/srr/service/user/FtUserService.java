package ggs.srr.service.user;

import ggs.srr.domain.FtUser;
import ggs.srr.repository.user.FtUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FtUserService {

    private final FtUserRepository repository;
    public Long save(FtUser user){
       return repository.save(user);
    }
    public FtUser findByIntraId(String intraId){
        return repository.findByIntraId(intraId);
    }
}
