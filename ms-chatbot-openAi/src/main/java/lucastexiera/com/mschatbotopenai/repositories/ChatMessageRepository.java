package lucastexiera.com.mschatbotopenai.repositories;

import lucastexiera.com.mschatbotopenai.model.ChatMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  @Query("SELECT m FROM ChatMessage m " +
          "WHERE m.conversation.userPhoneNumber = :phoneNumber " +
          "AND m.timestamp >= :startDate " +
          "ORDER BY m.timestamp ASC")
  List<ChatMessage> findRecentMessagesByPhoneNumber(
          @Param("phoneNumber") String phoneNumber,
          @Param("startDate") LocalDateTime startDate);
}
