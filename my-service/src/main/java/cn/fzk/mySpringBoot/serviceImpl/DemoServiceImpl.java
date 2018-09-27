package cn.fzk.mySpringBoot.serviceImpl;


import cn.fzk.mySpringBoot.application.moredatasource.TargetDataSource;
import cn.fzk.mySpringBoot.dto.secondary.DemoMappper;
import cn.fzk.mySpringBoot.entity.secentity.Demo;
import cn.fzk.mySpringBoot.service.DemoService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fzk on 2018/4/22.
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoMappper demoMappper;
    @TargetDataSource(value = "secondary")
    @Override
    public List<Demo> likeName(String name) {
        PageHelper.startPage(1,20);
        return demoMappper.likeName(name);
    }
}

