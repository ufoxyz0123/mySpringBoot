var websocket = null;
//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
    document.getElementById('message').innerHTML += innerHTML + '<br/>';
}
/**
 * 关闭聊天室
 * */
$(".btn-warning").on("click",function () {
    if(websocket){
        websocket.close();
    }
    $(".btn-success").attr("data-mark",0);
    $("#message").html("");
    $("#message").addClass("hide");
});
//发送消息
function send() {
    var message = document.getElementById("text").value;
    var toUser = document.getElementById("toUser").value;
    var socketMsg = {msg:message,toUser:toUser};
    if(toUser == ''){
        socketMsg.type =0;//群聊.
    }else{
        socketMsg.type =1;//单聊.
    }
    if(websocket){
        websocket.send(JSON.stringify(socketMsg));//将json对象转换为json字符串.
    }
}
