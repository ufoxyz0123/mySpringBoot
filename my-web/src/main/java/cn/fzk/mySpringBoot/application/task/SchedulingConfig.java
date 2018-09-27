package cn.fzk.mySpringBoot.application.task;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by Administrator on 2018/4/13.
 * 星号(*)
 *  可用在所有字段下，表示对应时间域名的每一个时刻，如*用在分钟字段，表示“每分钟”。
 * 问号(?)
 *  只能用在日期和星期字段，代表无意义的值，比如使用L设定为当月的最后一天，则配置日期配置就没有意义了，可用？作占位符的作用。
 * 减号(-)
 *  表示一个范围，如在日期字段5-10，表示从五号到10号，相当于使用逗号的5,6,7,8,9,10
 * 逗号(,)
 *  表示一个并列有效值，比如在月份字段使用JAN,DEC表示1月和12月
 * 斜杠(/)
 *  x/y表示一个等步长序列，x为起始值，y为增量步长值，如在小时使用1/3相当于1,4,7,10
 /**
  * L
  *  L(Last)只能在日期和星期字段使用，但意思不同。在日期字段，表示当月最后一天，在星期字段，表示星期六（如果按星期天为一星期的第一天的概念，星期六就是最后一天。
  *  如果L在星期字段，且前面有一个整数值X，表示“这个月的最后一个星期X”，比如3L表示某个月的最后一个星期二。
  * W
  *  选择离给定日期最近的工作日（周一至周五）。例如你指定“15W”作为day of month字段的值，就意味着“每个月与15号最近的工作日”。所以，如果15号是周六，则触发器会在14号（周五）触发。
  *  如果15号是周日，则触发器会在16号（周一）触发。如果15号是周二，则触发器会在15号（周二）触发。但是，如果你指定“1W”作为day of month字段的值，且1号是周六，则触发器会在3号（周一）触发。
  *  quartz不会“跳出”月份的界限。
  * LW组合
  *  在日期字段可以组合使用LW,表示当月最后一个工作日（周一至周五）
  * #
  *  只能在星期字段中使用指定每月第几个星期X。例如day of week字段的“6＃3”，就意味着“每月第3个星期五”（day3=星期五，＃3=第三个）；“2＃1”就意味着“每月第1个星期一”；“4＃5”就意味着“每月第5个星期3。
  *  需要注意的是“＃5”，如果在当月没有第5个星期三，则触发器不会触发。
  * C
  * 只能在日期和星期字段中使用，表示计划所关联的诶其，如果日期没有被关联，相当于日历中的所有日期，如5C在日期字段相当于5号之后的第一天，1C在日期字段使用相当于星期填后的第一天
  * */
@Configuration//启动时加载注解
@EnableScheduling//定时任务注解
public class SchedulingConfig {

    @Scheduled(cron = "0 0 0 * * ?") // 每天0点执行  fixedRate：上一次启动时间点之后 X秒执行一次,fixedDelay：上一次 结束时间点之后 每X秒执行一次,initialDelay：第一次延迟 X秒执行，之后按照fixedRate的规则每X秒执行
    public void scheduler() {
        System.out.println(">>>>>>>>> 这是定时任务：SchedulingConfig.scheduler()");
    }


    //rabbit操作类;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //@Scheduled(fixedDelay=3000)//3s执行1次此方法;未安装rabbitmq服务器故无法连接
    public void send(){
        rabbitTemplate.convertAndSend("foo","zhang");
    }


 }
