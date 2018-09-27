package cn.fzk.dubbo.serviceImpl;

import cn.fzk.dubbo.api.ApiService;
import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * Created by Administrator on 2018/9/12.
 */

@Service(version = "${mydubbo.service.version}")
public class ApiServiceImpl implements ApiService {


    @Override
    public UserInfo findByUsername(String username) {
        return null;
    }
}
