package cn.fzk.mySpringBoot.application.websocket;

import cn.fzk.mySpringBoot.vo.SocketMsg;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Administrator on 2018/6/20.
 */
@ServerEndpoint(value = "/websocket/{sid}/{nickname}")
@Component
public class WebSocketServer {
    private static Logger logger =  LoggerFactory.getLogger(WebSocketServer.class);//日志
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //用来记录sessionId和该session进行绑定
    private static Map<String,Session> map = new HashMap<String, Session>();
    //接收sid
    private String sid="";
    private String nickname="";
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("sid") String sid,@PathParam("nickname") String nickname) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        map.put(session.getId(), session);
        logger.info("有新窗口开始监听:"+sid+",当前在线人数为" + getOnlineCount());
        this.sid=sid;
        this.nickname=nickname;
        try {
            sendMessage("您已成功登陆聊天频道");
        } catch (IOException e) {
            logger.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        map.remove(session.getId());
        subOnlineCount();           //在线数减1
        logger.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("收到来自窗口"+sid+"的信息:"+message);
        //message 不是普通的string ，而是我们定义的SocketMsg json字符串.
        try {
            SocketMsg socketMsg = new ObjectMapper().readValue(message, SocketMsg.class);
            //单聊.
            if(socketMsg.getType() == 1){
                //单聊：需要找到发送者和接受者即可.
                socketMsg.setFromUser(session.getId());//发送者.
                //socketMsg.setToUser(toUser);//这个是由客户端进行设置.
                Session fromSession = map.get(socketMsg.getFromUser());
                Session toSession = map.get(socketMsg.getToUser());
                if(toSession != null){
                    //发送消息.
                    fromSession.getAsyncRemote().sendText(nickname+"："+socketMsg.getMsg());
                    toSession.getAsyncRemote().sendText(nickname+"："+socketMsg.getMsg());
                }else{
                    fromSession.getAsyncRemote().sendText("系统消息：对方不在线或者您输入的频道号有误");
                }
            }else {
                //群发给每个客户端.
                broadcast(socketMsg,nickname);
            }
        } catch (Exception e) {
            logger.error("推送消息到窗口"+sid+"，发生异常，具体异常：:"+e);
        }
    }

    /**
     * 群发的方法.
     */
    private void broadcast(SocketMsg socketMsg ,String nickname){
        for(WebSocketServer item:webSocketSet){
            //发送消息.
            item.session.getAsyncRemote().sendText(nickname+"："+socketMsg.getMsg());
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
        logger.info("推送消息到窗口"+sid+"，推送内容:"+message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else if(item.sid.equals(sid)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
