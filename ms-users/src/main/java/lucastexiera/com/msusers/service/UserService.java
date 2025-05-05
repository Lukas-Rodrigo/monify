package lucastexiera.com.msusers.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lucastexiera.com.msusers.dto.CategoryDTO;
import lucastexiera.com.msusers.infra.feign.FinanceClient;
import lucastexiera.com.msusers.model.User;
import lucastexiera.com.msusers.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FinanceClient  financeClient;



    @Transactional
    public User saveUser(User user) {
        var newUser = userRepository.save(user);
        financeClient.saveNewCategory(new CategoryDTO("Alimentação"), newUser.getId());
        return newUser;
    }


    public Long findUserIdByPhoneNumber(String phoneNumber) {
        var userFound = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("User not found"));
        return userFound.getId();
    }
}
