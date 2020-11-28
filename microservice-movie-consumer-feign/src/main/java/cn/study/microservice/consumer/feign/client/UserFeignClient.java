package cn.study.microservice.consumer.feign.client;

import cn.study.microservice.consumer.feign.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author jixuelei
 * @date 2020/11/19
 */
@FeignClient(name = "microservice-movie-provider")
public interface UserFeignClient {

    @GetMapping("/users/{id}")
    User findById(@PathVariable("id") Long id);
}
