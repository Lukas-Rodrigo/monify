package lucastexiera.com.msusers.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lucastexiera.com.msusers.dto.CategoryDTO;
import lucastexiera.com.msusers.dto.UserRequest;
import lucastexiera.com.msusers.dto.UserResponse;
import lucastexiera.com.msusers.infra.openfeign.FinanceMonifyClient;
import lucastexiera.com.msusers.model.User;
import lucastexiera.com.msusers.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FinanceMonifyClient financeMonifyClient;


    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }


    public Long findUserIdByPhoneNumber(String phoneNumber) {
        var userData = findByPhoneNumber(phoneNumber);
        return userData.getId();
    }

    private User findByPhoneNumber(String phoneNumber) {
        var userFound = userRepository.findByPhoneNumber(phoneNumber);
        return userFound.orElseThrow(() -> new RuntimeException("User not found"));
    }

}
