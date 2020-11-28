package cn.study.microservice.movie.consumer.controller;

import cn.study.microservice.movie.consumer.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author jixuelei
 * @date 2020/11/19
 */
@Slf4j
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Resource
    RestTemplate restTemplate;

    @GetMapping(value = "users/{id}")
    public User findById(@PathVariable("id") Long id) {
        String url = "http://localhost:8080/ms-user/user/{id}";
        return restTemplate.getForObject(url, User.class, id);
    }
}

