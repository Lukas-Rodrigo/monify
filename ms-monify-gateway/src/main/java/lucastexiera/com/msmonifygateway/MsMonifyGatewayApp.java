package lucastexiera.com.msmonifygateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MsMonifyGatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(MsMonifyGatewayApp.class, args);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/v1/webhook/whatsapp/**").uri("lb://msWhatsapp"))
                .route(r -> r.path("/v1/chatbot/message/**").uri("lb://ms-chatbot-openAi"))
                .route(r -> r.path("/v1/finance/category/**").uri("lb://ms-finance-monify"))
                .route(r -> r.path("/v1/finance/expense/**").uri("lb://ms-finance-monify"))
                .route(r -> r.path("/v1/users/**").uri("lb://ms-users"))
                .build();
    }

}
