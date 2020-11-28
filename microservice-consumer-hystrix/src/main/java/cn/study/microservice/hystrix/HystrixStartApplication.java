package cn.study.microservice.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * @author jixuelei
 * @date 2020/11/20
 */
@SpringBootApplication
@EnableCircuitBreaker // 开启Hystrix的 断路器
public class HystrixStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixStartApplication.class, args);
    }
}
