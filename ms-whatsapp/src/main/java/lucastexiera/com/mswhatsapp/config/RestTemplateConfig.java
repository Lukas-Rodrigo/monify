package lucastexiera.com.mswhatsapp.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${whatsapp.api.token}")
    private String WHATSAPP_API_TOKEN;

    RestTemplate restTemplate = new RestTemplate();

    @Bean
    public RestTemplate restTemplate() {
        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            request.getHeaders().setBearerAuth(WHATSAPP_API_TOKEN);
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return execution.execute(request, body);
        };
        restTemplate.getInterceptors().add(interceptor);
        return restTemplate;
    }
}
