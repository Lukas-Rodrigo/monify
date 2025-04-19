package lucastexiera.com.mschatbotopenai.repositories;

import lucastexiera.com.mschatbotopenai.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {
    Optional<Conversation> findTopByUserPhoneNumberOrderByIdDesc(String phoneNumber);

    Optional<Conversation> findByUserPhoneNumber(String userPhoneNumber);
}
