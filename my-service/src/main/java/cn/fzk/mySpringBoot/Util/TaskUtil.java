package cn.fzk.mySpringBoot.Util;

import cn.fzk.mySpringBoot.dto.primary.CronDto;
import cn.fzk.mySpringBoot.entity.primaryentity.Cron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2018/5/7.
 * 定时任务工具类
 */
@Component
public class TaskUtil {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());//日志
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    private CronDto cronDto;
    //future是treadPoolTaskScheduler执行方法schedule的返回值，主要用于定时任务的停止。
    private Map<String,ScheduledFuture<?>> future;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
    /**
     * 新增定时任务
     * */
    public ResModel startCron(String cron,Thread thread) {
        ResModel resModel = new ResModel(0,"操作成功");
        try{

            List<String> list = Stream.of(cronDto.findAll()).map(c->{
                return ((Cron)c).getCron();
            }).filter(cc-> !cc.equals(cron)).collect(Collectors.toList());
            if(list.size()>0){
                logger.error("新增定时任务错误，定时重复");
                resModel.setCode(-1);
                resModel.setDesc("新增定时任务错误，定时重复");
            }else{
                ScheduledFuture<?> future1 = threadPoolTaskScheduler.schedule(thread, new CronTrigger(cron));
                Cron c = new Cron();
                c.setCron(cron);
                c.setStatus(0);
                cronDto.save(c);
                future.put(cron,future1);
            }
        }catch (Exception e){
            logger.error("新增定时任务异常，具体异常：",e);
            resModel.setCode(-1);
            resModel.setDesc(e.getMessage());
        }

        return resModel;
    }
    /**
     * 关闭定时任务
     * */
    public ResModel stopCron(String cron) {
        ResModel resModel = new ResModel(0,"操作成功");
        try{
            if (future != null && future.size()>0) {
                if(future.get(cron) != null){
                    future.get(cron).cancel(true);
                }else{
                    logger.error("没有找到指定的定时任务");
                    resModel.setCode(-1);
                    resModel.setDesc("没有找到指定的定时任务");
                }
            }
        }catch (Exception e){
            logger.error("关闭定时任务异常，具体异常：",e);
            resModel.setCode(-1);
            resModel.setDesc(e.getMessage());
        }
        return resModel;
    }
    /**
     * 关闭之前动态创建的定时任务，重新在创建一个新的定时任务。
     * */
    public ResModel startCron10(String cron,Thread thread) {
        ResModel resModel = new ResModel(0,"操作成功");
        try{
            stopCron(cron);// 先停止，在开启.
            List<String> list = Stream.of(cronDto.findAll()).map(c->{
                return ((Cron)c).getCron();
            }).filter(cc-> !cc.equals(cron)).collect(Collectors.toList());
            if(list.size()>0){
                logger.error("新增定时任务错误，定时重复");
                resModel.setCode(-1);
                resModel.setDesc("新增定时任务错误，定时重复");
            }else{
                ScheduledFuture<?> future1 = threadPoolTaskScheduler.schedule(thread, new CronTrigger(cron));
                future.put(cron,future1);
            }
        }catch (Exception e){
            logger.error("重新定义定时任务异常，具体异常：",e);
            resModel.setCode(-1);
            resModel.setDesc(e.getMessage());
        }
        return resModel;
    }
    /**
     * 修改定时任务
     * 还不完善
     * */
    public ResModel startCron1(String cron,Thread thread) {
        ResModel resModel = new ResModel(0,"操作成功");
        try{
            threadPoolTaskScheduler.schedule(thread, (TriggerContext triggerContext)->{
                return new CronTrigger (cron).nextExecutionTime(triggerContext);
            });
        }catch (Exception e){
            logger.error("新增定时任务异常，具体异常：",e);
            resModel.setCode(-1);
            resModel.setDesc(e.getMessage());
        }

        return resModel;
    }
}
