package lucastexiera.com.mswhatsapp.controller;


import lombok.RequiredArgsConstructor;
import lucastexiera.com.mswhatsapp.dto.wppconnect.Qrcode;
import lucastexiera.com.mswhatsapp.service.StartApplicationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/whatsapp/start")
@RequiredArgsConstructor
public class StartApplicationController {

  private final StartApplicationService service;
  private String key;

  @PostMapping("{secretKey}")
  public Qrcode startApplication(
      @PathVariable String secretKey) {
    return service.genereteQrcode(secretKey);
  }
}
