package lucastexiera.com.monifyeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MonifyEurekaServerApp {

    public static void main(String[] args) {
        SpringApplication.run(MonifyEurekaServerApp.class, args);
    }

}
