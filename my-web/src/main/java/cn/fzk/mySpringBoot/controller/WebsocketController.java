package cn.fzk.mySpringBoot.controller;

import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.application.websocket.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * Created by Administrator on 2018/6/20.
 */
@Controller
@RequestMapping("/checkcenter")
public class WebsocketController {

    //页面请求
    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav=new ModelAndView("/socket");
        mav.addObject("cid", cid);
        return mav;
    }
    //推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public ResModel pushToWeb(@PathVariable String cid, String message) {
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            resModel.setCode(0);
            resModel.setDesc("消息推送成功");
            WebSocketServer.sendInfo(message,cid);
        } catch (IOException e) {
            resModel.setCode(-1);
            resModel.setDesc("消息推送异常，具体异常："+e);
        }
        return resModel;
    }
}
