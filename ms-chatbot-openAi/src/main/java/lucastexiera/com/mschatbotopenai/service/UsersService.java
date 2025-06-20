package lucastexiera.com.mschatbotopenai.service;

import lucastexiera.com.mschatbotopenai.dto.financemonify.CategoryDTO;
import lucastexiera.com.mschatbotopenai.infra.openfeign.FinanceClient;
import lucastexiera.com.mschatbotopenai.infra.openfeign.UsersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    @Autowired
    private UsersClient usersClient;

    @Autowired
    private FinanceClient financeClient;


    public List<CategoryDTO> findCategoriesByPhoneNumber(String phoneNumber) {
        var userID = usersClient.findUserIdByPhoneNumber(phoneNumber).getBody();
        return financeClient.findCategoriesByUserId(userID).getBody();
    }

    public Long findUserIDByPhoneNumber(String phoneNumber) {
        return usersClient.findUserIdByPhoneNumber(phoneNumber).getBody();
    }
}
