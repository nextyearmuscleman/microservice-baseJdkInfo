package cn.study.microservice.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author jixuelei
 * @date 2020/11/20
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigStartApplication.class, args);
    }
}
