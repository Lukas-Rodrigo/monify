package lucastexiera.com.msusers.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lucastexiera.com.msusers.model.User;
import lucastexiera.com.msusers.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;



    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }


    public Long findUserIdByPhoneNumber(String phoneNumber) {
        var userFound = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("User not found"));
        return userFound.getId();
    }
}
