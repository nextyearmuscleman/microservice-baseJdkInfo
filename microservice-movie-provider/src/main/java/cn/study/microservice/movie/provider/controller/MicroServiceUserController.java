package cn.study.microservice.movie.provider.controller;

import cn.study.microservice.movie.provider.dao.UserRepository;
import cn.study.microservice.movie.provider.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author jixuelei
 * @date 2020/11/19
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class MicroServiceUserController {

    @Resource
    UserRepository userRepository;


    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable(value = "id") Long id) {
        return userRepository.findById(id);
    }

}
