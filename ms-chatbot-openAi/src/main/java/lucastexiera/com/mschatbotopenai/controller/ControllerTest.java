package lucastexiera.com.mschatbotopenai.controller;

import lombok.extern.slf4j.Slf4j;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiMessageRequest;
import lucastexiera.com.mschatbotopenai.dto.chatbot.OpenAiRequestFactory;
import lucastexiera.com.mschatbotopenai.infra.openfeign.FinanceClient;
import lucastexiera.com.mschatbotopenai.model.ChatMessage;
import lucastexiera.com.mschatbotopenai.model.Conversation;
import lucastexiera.com.mschatbotopenai.repositories.ConversationRepository;
import lucastexiera.com.mschatbotopenai.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mapeamento")
@Slf4j
public class ControllerTest {

  @Autowired
  private ConversationRepository conversationRepository;

  @Autowired
  private ConversationService conversationService;

  @Autowired
  private FinanceClient financeClient;

//  @GetMapping
//  public OpenAiMessageRequest mapeamento() {
//
//    var conversation = conversationRepository.findByUserPhoneNumber("557581484284").get();
//    var categories= financeClient.findCategoriesByUserId(1L).getBody();
//    var request = OpenAiRequestFactory.instance(conversation, categories);
//    return request;
//  }

  @GetMapping("chat")
  public OpenAiMessageRequest chat() {
    var conversation = conversationService.getRecentMessagesByUserPhoneNumber("557581484284", 2);

    var userCategories = financeClient.findCategoriesByUserId(9L).getBody();
    log.info(userCategories.toString());
    return OpenAiRequestFactory.instance(conversation, userCategories);
  }
}
