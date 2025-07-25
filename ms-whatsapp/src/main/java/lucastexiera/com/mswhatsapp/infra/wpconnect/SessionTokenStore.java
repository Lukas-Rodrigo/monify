package lucastexiera.com.mswhatsapp.infra.wpconnect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionTokenStore {

  private String token;
  private String sessionName;
}
