var poster = {
    /**
     * 发送邮件
     * */
    sendEmail:function (param){
        $.ajax({
            url:url.sendEmail(),
            data:param,
            async:false,//不异步
            type:'post',
            success:function (data) {
                if(data.code == 0){
                    alert("发送成功");
                } else{
                    alert(data.desc);
                }
            }
        });
    },
    /**
     * 多文件上传
     * */
    moreUpload:function (param) {
        var formFile = new FormData(param);
        $.ajax({
            url:url.moreUpload(),
            data:formFile,
            async:true,//异步
            type:'post',
            dataType: "json",
            cache: false,//上传文件无需缓存
            processData: false,//用于对data参数进行序列化处理 这里必须false
            contentType: false, //必须
            success:function (data) {
                if(data.code == 0){
                    alert("上传成功");
                } else{
                    alert(data.desc);
                }
            }
        });
    },
    /**
     * 获得验证码
     * */
    changeValidatecode:function (param) {
        var xmlhttp;
        xmlhttp=new XMLHttpRequest();
        xmlhttp.open("GET",url.changeValidatecode(),true);
        xmlhttp.responseType = "blob";
        xmlhttp.onload = function(){
            if (this.status == 200) {
                var blob = this.response;
                var img = document.createElement("img");
                img.onload = function(e) {
                    window.URL.revokeObjectURL(img.src);
                };
                img.src = window.URL.createObjectURL(blob);
                $(param).attr("src",img.src);
            }
        }
        xmlhttp.send();
    },
    /**
     * 跳转注册账号
     * */
    registerUser:function () {
        layui.use('layer', function(){
            var layer= layui.layer//获取layer模块
            layer.open({
                type:2,
                title:'账号注册',
                content:url.registerUser(),
                area:['600px','320px']
            });
        });
    },
    /**
     * 登录
     * */
    login:function (param) {
        $(param).attr("action",url.login());
        $(param).attr("method",'post');
        $(param).submit();
    },
    /**
     * 注册账号
     * */
    saveUser:function (param) {
        $("#passWordMd5").val(hex_md5($("#passWord").val()));
        $.ajax({
            url:url.saveUser(),
            data:param.serialize(),
            async:false,//不异步
            type:'post',
            success:function (data) {
                layui.use('layer', function(){
                    var layer= layui.layer//获取layer模块
                    if(data.code == 0){
                        layer.msg(data.desc,{time:1000},function () {
                            parent.layer.closeAll();
                        });
                    } else{
                        layer.msg(data.desc,{time:1000});
                    }
                });
            }
        });
    },
    /**
     * 切换语言
     * */
    changeSessionLanauage:function (param) {
        $.ajax({
            url:url.changeSessionLanauage(),
            data:{"lang":param},
            async:true,//同步
            type:'get',
            dataType: "json",
            success:function (data) {
                if(data.code == 0){
                    window.location.reload()
                }
            }
        });
    },
    /**
     * 聊天室
     * */
    websocket:function (param,cid,nickname) {
        if($(param).attr("data-mark") == 0){
            //判断当前浏览器是否支持WebSocket
            if ('WebSocket'in window) {
                websocket = new WebSocket("wss://localhost:443"+url.websocket()+"/"+cid+"/"+nickname);
            } else {
                layui.use('layer', function(){
                    var layer= layui.layer//获取layer模块
                    layer.msg("当前浏览器不支持次功能，请更换浏览器",{time:1000},function () {
                        layer.closeAll();
                    });
                });
                return;
            }
            websocket.onopen = function() {
                $("#message").removeClass("hide");
            };
            //获得消息事件
            websocket.onmessage = function(msg) {
                setMessageInnerHTML(event.data);
            };
            //关闭事件
            websocket.onclose = function() {
                setMessageInnerHTML("Loc MSG:关闭连接");
            };
            //发生了错误事件
            websocket.onerror = function() {
                alert("Socket发生了错误");
                //此时可以尝试刷新页面
            }
            window.onbeforeunload = function() {
                websocket.close();
            }
            $(param).attr("data-mark",1);
        }
    },
    /**
     * 更改用户账号状态
     * */
    changeStatus:function (id,status,userName) {
        $.ajax({
            url:url.changeStatus(),
            data:{"uid":id,"status":status,"userName":userName},
            async:false,//同步
            type:'post',
            dataType: "json",
            success:function (data) {
                if(data.code == 0){
                    $("#userlist").bootstrapTable('refresh');
                }
                layui.use('layer', function(){
                    var layer= layui.layer//获取layer模块
                    layer.msg(data.desc,{time:2000})
                });
            }
        });
    },
    /**
     * 跳转用户详情与修改
     * */
    editAndView:function (id,username) {
        layui.use('layer', function(){
            var layer= layui.layer//获取layer模块
            layer.open({
                type:2,
                title:'用户详情',
                content:url.editAndView(id,username),
                area:['600px','400px']
            });
        });
    },
    /**
     * 用户信息修改
     * */
    updateUser:function (param) {
        $.ajax({
            url:url.updateUser(),
            data:param.serialize(),
            async:false,//同步
            type:'post',
            dataType: "json",
            success:function (data) {
                layui.use('layer', function(){
                    var layer= layui.layer//获取layer模块
                    layer.msg(data.desc,{time:2000})
                });
                if(data.code == 0){
                    $("#userlist").bootstrapTable('refresh');
                    setTimeout("parent.layer.closeAll()","2000");
                }
            }
        });
    },
    /**
     * 角色状态修改
     * */
    roleChangeStatus:function (id,status) {
        $.ajax({
            url:url.roleChangeStatus(),
            data:{"roleId":id,"available":status},
            async:false,//同步
            type:'post',
            dataType: "json",
            success:function (data) {
                if(data.code == 0){
                    $("#rolelist").bootstrapTable('refresh');
                }
                layui.use('layer', function(){
                    var layer= layui.layer//获取layer模块
                    layer.msg(data.desc,{time:2000})
                });
            }
        });
    },
    /**
     * 跳转角色添加、详情与修改
     * */
    roleEditAndView:function (id) {
        var desc = "新增角色";
        if(id){
            desc = "角色详情"
        }
        layui.use('layer', function(){
            var layer= layui.layer//获取layer模块
            layer.open({
                type:2,
                title:desc,
                content:url.roleEditAndView(id),
                area:['600px','400px']
            });
        });
    },
    /**
     * 角色添加、详情与修改
     * */
    doRoleEditAndView:function (param) {
        $.ajax({
            url:url.doRoleEditAndView(),
            data:param.serialize(),
            async:false,//同步
            type:'post',
            dataType: "json",
            success:function (data) {
                if(data.code == 0){
                    parent.$("#rolelist").bootstrapTable('refresh');
                    setTimeout("parent.layer.closeAll()","2000");
                }
                layui.use('layer', function(){
                    var layer= layui.layer//获取layer模块
                    layer.msg(data.desc,{time:2000})
                });
            }
        });
    },
    /**
     * 删除角色
     * */
    delRole:function (id) {
        $.ajax({
            url:url.delRole(),
            data:{"roleId":id},
            async:false,//同步
            type:'post',
            dataType: "json",
            success:function (data) {
                if(data.code == 0){
                    $("#rolelist").bootstrapTable('refresh');
                    setTimeout("parent.layer.closeAll()","2000");
                }
                layui.use('layer', function(){
                    var layer= layui.layer//获取layer模块
                    layer.msg(data.desc,{time:2000})
                });
            }
        });
    },
    /**
     * 跳转新增、详情与修改权限
     * */
    peiEditAndView:function (id) {
        var desc = "新增权限";
        if(id){
            desc = "权限详情";
        }
        layui.use('layer', function(){
            var layer= layui.layer//获取layer模块
            layer.open({
                type:2,
                title:desc,
                content:url.peiEditAndView(id),
                area:['600px','350px']
            });
        });
    },
    /**
     * 更改权限状态
     * */
    pieChangeStatus:function (id,status) {
        $.ajax({
            url:url.pieChangeStatus(),
            data:{"permissionId":id,"available":status},
            async:false,//同步
            type:'post',
            dataType: "json",
            success:function (data) {
                if(data.code == 0){
                    $("#permissionlist").bootstrapTable('refresh');
                }
                layui.use('layer', function(){
                    var layer= layui.layer//获取layer模块
                    layer.msg(data.desc,{time:2000})
                });
            }
        });
    },
    /**
     * 新增、详情与修改权限
     * */
    doPerEditAndView:function (param) {
        $.ajax({
            url:url.doPerEditAndView(),
            data:param.serialize(),
            async:false,//同步
            type:'post',
            dataType: "json",
            success:function (data) {
                if(data.code == 0){
                    parent.$("#permissionlist").bootstrapTable('refresh');
                    setTimeout("parent.layer.closeAll()","2000");
                }
                layui.use('layer', function(){
                    var layer= layui.layer//获取layer模块
                    layer.msg(data.desc,{time:2000})
                });
            }
        });
    },
    /**
     * 删除权限
     * */
    delPer:function (id) {
        $.ajax({
            url:url.delPer(),
            data:{"permissionId":id},
            async:false,//同步
            type:'post',
            dataType: "json",
            success:function (data) {
                if(data.code == 0){
                    $("#permissionlist").bootstrapTable('refresh');
                    setTimeout("parent.layer.closeAll()","2000");
                }
                layui.use('layer', function(){
                    var layer= layui.layer//获取layer模块
                    layer.msg(data.desc,{time:2000})
                });
            }
        });
    },
    /**
     * 时间格式化
     *
     * @export
     * @param {number} [timestamp=Date.now()] 时间戳
     * @param {string} [fmt='yyyy-MM-dd'] 时间的格式
     * @returns
     * 调用：
     * var time1 = dateFormat(+new Date(),“yyyy-MM-dd”);
     * var time2 = dateFormat( 1499399860611 ,“yyyy-MM-dd hh:mm:ss”);
     */
    dateFormart:function (date,fmt) {
        var date = new Date(date);
        var o = {
            "M+" : date.getMonth()+1,                 //月份
            "d+" : date.getDate(),                    //日
            "h+" : date.getHours(),                   //小时
            "m+" : date.getMinutes(),                 //分
            "s+" : date.getSeconds(),                 //秒
            "q+" : Math.floor((date.getMonth()+3)/3), //季度
            "S"  : date.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    }
}