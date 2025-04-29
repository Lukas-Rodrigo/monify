package lucastexiera.com.mschatbotopenai.controller;

import lucastexiera.com.mschatbotopenai.service.AnswersForUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ControllerTest {

    @Autowired
    private AnswersForUsersService  answersForUsersService;



}

