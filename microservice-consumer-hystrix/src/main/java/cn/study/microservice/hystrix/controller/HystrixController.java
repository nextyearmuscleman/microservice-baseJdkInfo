package cn.study.microservice.hystrix.controller;

import cn.study.microservice.hystrix.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author jixuelei
 * @date 2020/11/20
 */
@RestController
@RequestMapping("/hystrix")
@Slf4j
public class HystrixController {

    @Resource
    RestTemplate restTemplate;


    /**
     * @HystrixCommand 注解标注的方法可以实现断路器的功能，
     * 属性fallbackMethod： 指定一个处理fallback的逻辑方法
     */
    @HystrixCommand(fallbackMethod = "findByIdFallback")
    @GetMapping(value = "users/{id}")
    public User findById(@PathVariable("id") Long id) {

        return restTemplate.getForObject("http://microservice-movie-provider/users/{id}",
                User.class,
                id);
    }

    public User findByIdFallback(Long id, Throwable throwable) {
        log.error("error start ", throwable);
        return new User(id, "default", "default", 0, new BigDecimal(1));
    }
}
