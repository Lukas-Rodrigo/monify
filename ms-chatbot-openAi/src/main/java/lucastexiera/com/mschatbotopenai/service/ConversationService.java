package lucastexiera.com.mschatbotopenai.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lucastexiera.com.mschatbotopenai.model.ChatMessage;
import lucastexiera.com.mschatbotopenai.model.Conversation;
import lucastexiera.com.mschatbotopenai.repositories.ChatMessageRepository;
import lucastexiera.com.mschatbotopenai.repositories.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;


    @Transactional
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

    @Transactional
    public Conversation saveAssistantMessage(String userPhoneNumber, String message) {
        ChatMessage assistantMsg = new ChatMessage();
        assistantMsg.setSender("assistant");
        assistantMsg.setMessage(message);
        assistantMsg.setTimestamp(LocalDateTime.now());

        Conversation conversation = conversationRepository
                .findByUserPhoneNumber(userPhoneNumber)
                .orElseGet(() -> createNewConversation(userPhoneNumber));

        assistantMsg.setConversation(conversation);
        conversation.getMessages().add(assistantMsg);
        return conversationRepository.save(conversation);
    }

    public Conversation findByUserPhoneNumber(String userPhoneNumber) {
        return conversationRepository.findByUserPhoneNumber(userPhoneNumber).orElseThrow(
                () -> new EntityNotFoundException("User phone number " + userPhoneNumber + " not found")
        );
    }

    public List<ChatMessage> getRecentMessagesByUserPhoneNumber(String userPhoneNumber, int days) {
        LocalDateTime daysAgo = LocalDateTime.now().minusDays(days);
        return chatMessageRepository.findRecentMessagesByPhoneNumber(userPhoneNumber, daysAgo);


    }

    private Conversation createNewConversation(String userPhoneNumber) {
        Conversation conv = new Conversation();
        conv.setUserPhoneNumber(userPhoneNumber);
        conv.setMessages(new ArrayList<>());
        return conv;
    }

}
