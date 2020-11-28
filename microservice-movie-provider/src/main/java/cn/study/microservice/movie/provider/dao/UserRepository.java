package cn.study.microservice.movie.provider.dao;

import cn.study.microservice.movie.provider.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author jixuelei
 * @date 2020/11/19
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
