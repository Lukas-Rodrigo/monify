package lucastexiera.com.msfinancemonify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsFinanceMonifyApp {

    public static void main(String[] args) {
        SpringApplication.run(MsFinanceMonifyApp.class, args);
    }

}
