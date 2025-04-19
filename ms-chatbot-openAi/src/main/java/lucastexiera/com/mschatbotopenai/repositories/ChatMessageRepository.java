package lucastexiera.com.mschatbotopenai.repositories;

import lucastexiera.com.mschatbotopenai.model.ChatMessage;
import lucastexiera.com.mschatbotopenai.model.Conversation;
import lucastexiera.com.mschatbotopenai.service.ConversationService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
