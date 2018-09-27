package cn.fzk.mySpringBoot.controller;


import cn.fzk.mySpringBoot.dto.mongodb.MongoInfoDto;
import cn.fzk.mySpringBoot.entity.mongodb.MongoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */
@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    private MongoInfoDto mongoInfoDto;
    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping("save")
    public String save(){
        MongoInfo demoInfo = new MongoInfo();
        demoInfo.setName("张三");
        demoInfo.setAge(20);
        mongoInfoDto.save(demoInfo);

        demoInfo = new MongoInfo();
        demoInfo.setName("李四");
        demoInfo.setAge(30);
        mongoInfoDto.save(demoInfo);

        return "ok";
    }
    @RequestMapping("find")
    public List<MongoInfo> find(){
        return mongoInfoDto.findAll();
    }


    @RequestMapping("findByName")
    public MongoInfo findByName(){
        return mongoInfoDto.findByName("张三");
    }
    @RequestMapping("find2")
    public List<MongoInfo> find2(){
        return mongoTemplate.findAll(MongoInfo.class);
    }
}
