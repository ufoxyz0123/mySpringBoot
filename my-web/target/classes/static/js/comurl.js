var url = {
    /**
     * 发送邮件
     * */
    sendEmail:function () {
        return "/sendEmail";
    },
    /**
     * 多文件上传
     * */
    moreUpload:function () {
        return "/batch/upload";
    },
    /**
     * 验证码
     * */
    changeValidatecode:function () {
        return "/myServlet2/validatecodeServlet";
    },
    /**
     * 跳转注册账号
     * */
    registerUser:function () {
        return "/user/registerUser"
    },
    /**
     * 登录
     * */
    login:function () {
        return "/login"
    },
    /**
     * 注册账号
     * */
    saveUser:function () {
        return "/user/saveUser";
    },
    /**
     * 更改语言
     * */
    changeSessionLanauage:function () {
        return "/changeSessionLanauage";
    },
    /**
     * 聊天室
     * */
    websocket:function () {
        return "/websocket";
    },
    /**
     * 管理员更改账号状态
     * */
    changeStatus:function () {
        return "/user/changeStatus";
    },
    /**
     * 跳转用户详情与修改
     * */
    editAndView:function (id,username) {
        return "/user/editAndView?uid="+ id +"&username=" + username;
    },
    /**
     * 修改用户信息（包括角色）
     * */
    updateUser:function () {
        return "/user/updateUser";
    },
    /**
     * 角色状态修改
     * */
    roleChangeStatus:function () {
        return "/role/roleChangeStatus";
    },
    /**
     * 跳转角色新增、详情与修改
     * */
    roleEditAndView:function (id) {
        if (id) {
            return "/role/roleEditAndView?rid=" + id;
        } else {
            return "/role/roleEditAndView";
        }
    },
    /**
     * 角色新增、详情与修改
     * */
    doRoleEditAndView:function () {
        return  "/role/doRoleEditAndView";
    },
    /**
     * 删除角色
     * */
    delRole:function () {
        return "/role/delRole";
    },
    /**
     * 跳转新增、详情与修改权限
     * */
    peiEditAndView:function (id) {
        if (id) {
            return "/permission/peiEditAndView?permissionId=" + id;
        } else {
            return "/permission/peiEditAndView";
        }
    },
    /**
     * 更改权限状态
     * */
    pieChangeStatus:function () {
        return "/permission/preChangeStatus";
    },
    /**
     * 新增、详情与修改权限
     * */
    doPerEditAndView:function () {
        return "/permission/doPerEditAndView";
    },
    /**
     * 删除权限
     * */
    delPer:function () {
        return "/permission/delPer";
    }
}
