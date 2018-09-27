package cn.fzk.mySpringBoot.service;

import cn.fzk.mySpringBoot.entity.secentity.Demo;

import java.util.List;

/**
 * Created by fzk on 2018/4/22.
 */
public interface DemoService {
    public List<Demo> likeName(String name);
}
