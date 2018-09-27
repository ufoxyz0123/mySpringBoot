package cn.fzk.mySpringBoot.dto.mongodb;


import cn.fzk.mySpringBoot.entity.mongodb.MongoInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Administrator on 2018/4/28.
 */
public interface MongoInfoDto extends MongoRepository<MongoInfo, String> {
    MongoInfo findByName(String name);
}
