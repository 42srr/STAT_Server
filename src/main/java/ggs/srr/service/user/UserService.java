package ggs.srr.service.user;

import ggs.srr.domain.user.FtUser;
import ggs.srr.repository.user.UserRepository;
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

    public FtUser findById(long id) {
        return userRepository.findById(id);
    }

    public Optional<FtUser> findByIntraId(String intraId){
        return userRepository.findByIntraId(intraId);
    }

    public List<FtUser> findAll(){
        return userRepository.findAll();
    }
}
