package lucastexiera.com.mschatbotopenai.service;

import lucastexiera.com.mschatbotopenai.model.ChatMessage;
import lucastexiera.com.mschatbotopenai.model.Conversation;
import lucastexiera.com.mschatbotopenai.repositories.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    // refatorar.

    public Conversation saveUserMessage(String userPhoneNumber, String message) {
        ChatMessage userMsg = new ChatMessage();
        userMsg.setSender("user");
        userMsg.setMessage(message);
        userMsg.setTimestamp(LocalDateTime.now());

        Conversation conversation = conversationRepository
                .findByUserPhoneNumber(userPhoneNumber)
                .orElseGet(() -> createNewConversation(userPhoneNumber));

        userMsg.setConversation(conversation);
        conversation.getMessages().add(userMsg);
        return conversationRepository.save(conversation);
    }

    public void saveAssistantMessage(String userPhoneNumber, String message) {
        ChatMessage assistantMsg = new ChatMessage();
        assistantMsg.setSender("assistant");
        assistantMsg.setMessage(message);
        assistantMsg.setTimestamp(LocalDateTime.now());

        Conversation conversation = conversationRepository
                .findByUserPhoneNumber(userPhoneNumber)
                .orElseGet(() -> createNewConversation(userPhoneNumber));

        assistantMsg.setConversation(conversation);
        conversation.getMessages().add(assistantMsg);
        conversationRepository.save(conversation);
    }

    private Conversation createNewConversation(String userPhoneNumber) {
        Conversation conv = new Conversation();
        conv.setUserPhoneNumber(userPhoneNumber);
        conv.setMessages(new ArrayList<>());
        return conv;
    }

}
