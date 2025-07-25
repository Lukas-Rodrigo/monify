package lucastexiera.com.mswhatsapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lucastexiera.com.mswhatsapp.dto.wppconnect.Qrcode;
import lucastexiera.com.mswhatsapp.dto.wppconnect.StartSessionToken;
import lucastexiera.com.mswhatsapp.infra.wpconnect.SessionTokenStore;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartApplicationService {

  private final RestTemplate restTemplate = new RestTemplate();
  private final String BASE_URL = "http://wppconnect-server:21465/api";
  private final SessionTokenStore tokenStore;


  public Qrcode genereteQrcode(String secretKey) {
    String url = BASE_URL+"/monify-session2/{sessionKey}/generate-token";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    var response = restTemplate.exchange(
        url,
        HttpMethod.POST,
        requestEntity,
        StartSessionToken.class,
        secretKey
    ).getBody();

    tokenStore.setToken(response.token());
    tokenStore.setSessionName(response.session());

    return genereteQrcode(response);
  }

  private Qrcode genereteQrcode(StartSessionToken sessionToken) {

    var qrcode = callStartSession(sessionToken);
    if (qrcode == null) {
      return callStartSession(sessionToken);
    }
    return qrcode;
  }

  private Qrcode callStartSession(StartSessionToken sessionToken) {
    String url = BASE_URL + "/{sessionName}/start-session";
    var token = sessionToken.token();
    var sessionName = sessionToken.session();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(
        token
    );
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    return restTemplate
        .exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            Qrcode.class,
            sessionName
        )
        .getBody();
  }

}
