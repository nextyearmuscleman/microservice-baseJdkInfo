package cn.study.microservice.movie.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author jixuelei
 * @date 2020/11/19
 */

/**
 * @SpringBootApplication是一个组合注解，
 * 它整合了
 *  @Configuration
 *  @EnableAutoConfiguration
 *  @ComponentScan注解，并开启了Spring Boot程序的组件扫描和自动配置功能。
 *  在开发Spring Boot程序的过程中，常常会组合使用@Configuration、@EnableAutoConfiguration和@ComponentScan等注解，
 *  所以Spring Boot提供了@SpringBootApplication，来简化开发。
 */
@SpringBootApplication
public class RibbonStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonStartApplication.class, args);
    }


    /**
     * @SpringBootApplication 包含了@Configuration注解，标识一个配置类
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
