$(function () {
    $("#userlist").bootstrapTable();
    /*  var fso, drv, s ="";
     fso = new ActiveXObject("Scripting.FileSystemObject");
     drv = fso.GetDrive(fso.GetDriveName("c:\\"));
     s += "Drive C:" + " - ";
     s += drv.VolumeName + "\n";
     s += "Total Space: " + drv.TotalSize / 1024;
     s += " Kb" + "\n";
     s += "Free Space: " + drv.FreeSpace / 1024;
     s += " Kb" + "\n";
     alert(s);*/
});
/**
 * 列表查询参数
 * */
function queryParams(params) {
    return $("#search-form").serialize() +"&order="+params.order+"&offset="+params.offset+"&limit="+params.limit;
}
/**
 * 查询
 * */
$(".search-btn").on('click',function (e) {
    $("#userlist").bootstrapTable('refresh');
});
/**
 * 下标格式化
 * */
function indexFormatter(value,row,index) {
    return index +1 ;
}
/**
 * 实名格式化
 * */
function certificationFormatter(value,row,index) {
    if(row.certification == 1 ){
        value = "已实名" ;
    }else{
        value = "未实名" ;
    }
    return value;
}
/**
 * 状态格式化
 * */
function statusFormatter(value,row,index) {
    if(row.status==0 ){
        value = "正常用户" ;
    }else{
        value = "冻结用户" ;
    }
    return value;
}
/**
 * 创建时间格式化
 * */
function createTimeFormatter(value,row,index) {
    value = poster.dateFormart(row.createTime,"yyyy-MM-dd")
    return value;
}
/**
 * 操作格式化
 * */
function optionFormatter(value,row,index) {
    var strValue = "<span class='show_btn'>查看<div class='show_btn_div'><a class='J_look' title='查看' onclick='editAndView(\""+row.uid+"\",\""+row.userName+"\")'>查看</a>";
    if(row.status == 0){
        strValue +="<a class='J_update' title='禁用' onclick='changeStatus(\""+row.uid+"\",\""+1+"\",\""+row.userName+"\")'>禁用</a></div></span>";
    }else {
        strValue +="<a class='J_update' title='启用' onclick='changeStatus(\""+row.uid+"\",\""+0+"\",\""+row.userName+"\")'>启用</a></div></span>";
    }
    return strValue;
}
/**
 * 更改账号状态
 * */
function changeStatus(id,status,userName) {
    poster.changeStatus(id,status,userName);
}
/**
 * 用户详情与修改
 * */
function editAndView(id,username) {
    poster.editAndView(id,username);
}
/**
 * 禁用、启用账号
 * */
$(".userTable").on('click','.changeStatus',function (e) {
    var id = $(e.target).data("id");
    var status = $(e.target).data("value");
    var userName = $(e.target).data("username");
    poster.changeStatus(id,status,userName);
});
