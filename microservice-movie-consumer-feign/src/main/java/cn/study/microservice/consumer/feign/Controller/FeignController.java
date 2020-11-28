package cn.study.microservice.consumer.feign.Controller;

import cn.study.microservice.consumer.feign.client.UserFeignClient;
import cn.study.microservice.consumer.feign.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author jixuelei
 * @date 2020/11/19
 */
@RestController
@RequestMapping
public class FeignController {

    @Resource
    UserFeignClient feignClient;

    @GetMapping("/users/{id}")
    public User findById(@PathVariable Long id) {
        return this.feignClient.findById(id);
    }
}
